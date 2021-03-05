package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.model.Ad;
import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.model.Person;
import mk.ukim.finki.recruitment.model.User;
import mk.ukim.finki.recruitment.model.enumerations.Role;
import mk.ukim.finki.recruitment.model.exceptions.AdAlreadySavedException;
import mk.ukim.finki.recruitment.service.AdService;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/profiles")
public class ProfileListController {

    private UserService userService;
    private AdService adService;

    public ProfileListController(UserService userService, AdService adService) {
        this.userService = userService;
        this.adService = adService;
    }

    @GetMapping
    public String getProfileListPage(HttpServletRequest request, Model model) {
        addUsernameParam(request, model);

        List<User> users = this.userService.getAllUsers();

        if(users.size() == 0) model.addAttribute("noProfilesExist", true);

        model.addAttribute("profiles", users);

        model.addAttribute("bodyContent", "profileList");
        return "master-template";
    }

    @GetMapping("/{uuid}")
    public String getProfilePage(@RequestParam(required = false) String error,
                                 @PathVariable String uuid,
                                 HttpServletRequest request, Model model) {
        if(error != null && !error.isEmpty()) model.addAttribute("hasError", true);

        addUsernameParam(request, model);
        modifyModel(uuid, request, model);

        model.addAttribute("bodyContent", "profileSecondary");
        return "master-template";
    }

    @GetMapping("/company_buttons_front/{id}/{uuid}")
    public String adInterestedOrEmail(@PathVariable Long id,
                                      @PathVariable String uuid,
                                      @RequestParam String personButton,
                                      HttpServletRequest request) {
        if(personButton.equals("interested")) {
            try {
                this.adService.save(request.getRemoteUser(), id);
            }
            catch (AdAlreadySavedException exception) {
                return "redirect:/profiles/" + uuid + "?error=" + exception.getMessage();
            }
        }
        else {
            Company adOwner = this.adService.getAdOwner(id);
            return "redirect:/email?address=" + adOwner.getEmail() + "&subject=" + this.adService.findById(id).getHeader()
                    + "&profileFlag=true";
        }

        return "redirect:/profile";
    }

    @GetMapping("/company_buttons_back/{id}/{uuid}")
    public String adEditOrDelete(@PathVariable Long id,
                                 @PathVariable String uuid,
                                 @RequestParam String otherButton) {
        if(otherButton.equals("delete")) this.adService.delete(id);
        else {
            Ad ad = this.adService.findById(id);
            return "redirect:/adedit?adId=" + ad.getId() + "&header=" + ad.getHeader() + "&body=" + ad.getBody() + "&profileFlag=true"
                    + "&companyId=" + ad.getCompany().getId();
        }

        return "redirect:/profiles/" + uuid;
    }

    @PostMapping
    public String filterProfileListPage(@RequestParam String profileSelect,
                                  HttpServletRequest request, Model model) {
        addUsernameParam(request, model);

        if(profileSelect.equals("all")) return "redirect:/profiles";
        else if(profileSelect.equals("company")) {
            List<Company> companyProfiles = this.userService.getCompanyProfiles();

            if(companyProfiles.size() == 0) model.addAttribute("noProfilesFound", true);

            model.addAttribute("profiles", companyProfiles);
        }
        else {
            List<Person> personProfiles = this.userService.getPersonProfiles();

            if(personProfiles.size() == 0) model.addAttribute("noProfilesFound", true);

            model.addAttribute("profiles", personProfiles);
        }

        model.addAttribute("bodyContent", "profileList");
        return "master-template";
    }

    @PostMapping("/search")
    public String searchProfileListPage(@RequestParam(required = false) String queryString,
                                        HttpServletRequest request, Model model) {
        if(queryString == null || queryString.isEmpty()) return "redirect:/profiles";

        addUsernameParam(request, model);

        List<User> users = this.userService.getUsersByQueryString(queryString);

        if(users.size() == 0) model.addAttribute("notFoundByQuery", true);

        model.addAttribute("profiles", users);

        model.addAttribute("bodyContent", "profileList");
        return "master-template";
    }

    // ==================================================== //

    public void addUsernameParam(HttpServletRequest request, Model model) {
        User user = this.userService.getUserInstanceByUUID(request.getRemoteUser());

        model.addAttribute("username",
                user instanceof Person
                        ? user.getUsername()
                        : user.getName());
    }

    public void modifyModel(String uuid, HttpServletRequest request, Model model) {
        User user = this.userService.getUserInstanceByUUID(uuid);
        User currentUser = this.userService.getUserInstanceByUUID(request.getRemoteUser());

        modifyModelByUser(user, model);
        modifyModelByCurrentUser(currentUser, model);

        if (user instanceof Person && user.getRole() == Role.ROLE_ADMIN) {
            model.addAttribute("profileUsername", user.getName());
        }
        else if(user instanceof Person) {
            model.addAttribute("profileUsername", user.getName());
            model.addAttribute("personAds", this.adService.getAdsByPersonUsername(user.getUsername()));
        }
        else  {
            model.addAttribute("profileUsername", user.getName());
            model.addAttribute("companyId", ((Company) user).getId());
            model.addAttribute("companyAds", this.adService.getAdsByCompanyId(((Company) user).getId()));
        }

        model.addAttribute("bio", user.getBio());
        model.addAttribute("role", user.getAccountRole());
        model.addAttribute("imgsrc", user.getImageSourceUrl());
        model.addAttribute("email", user.getEmail());
    }

    public void modifyModelByCurrentUser(User currentUser, Model model) {
        if (currentUser instanceof Person && currentUser.getRole() == Role.ROLE_ADMIN) {
            model.addAttribute("isCurrentUserAdmin", true);
            model.addAttribute("isPerson", false);
        }
        else if(currentUser instanceof Person) {
            model.addAttribute("isPerson", true);
        }
        else  {
            model.addAttribute("isCurrentUserCompany", true);
            model.addAttribute("isPerson", false);
        }
    }

    public void modifyModelByUser(User user, Model model) {
        if (user instanceof Person && user.getRole() == Role.ROLE_ADMIN) {
            model.addAttribute("isAdmin", true);
            model.addAttribute("uuid", user.getUsername());
        }
        else if(user instanceof Person) {
            model.addAttribute("isPerson", true);
            model.addAttribute("uuid", user.getUsername());
        }
        else  {
            model.addAttribute("isCompany", true);
            model.addAttribute("uuid", ((Company) user).getId());
        }
    }

}
