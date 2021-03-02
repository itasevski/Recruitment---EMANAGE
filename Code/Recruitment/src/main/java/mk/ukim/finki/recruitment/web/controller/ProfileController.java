package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.model.Person;
import mk.ukim.finki.recruitment.model.User;
import mk.ukim.finki.recruitment.model.enumerations.Role;
import mk.ukim.finki.recruitment.service.AdService;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    public static final String targetFolderImagePPPath = "C:\\Users\\User\\Desktop\\Recruitment---EMANAGE\\Code\\Recruitment\\target\\classes\\static\\customProfilePictures\\";

    private UserService userService;
    private AdService adService;

    public ProfileController(UserService userService, AdService adService) {
        this.userService = userService;
        this.adService = adService;
    }

    @GetMapping
    public String getProfilePage(HttpServletRequest request, Model model) {
        modifyModel(request, model);

        model.addAttribute("bodyContent", "profile");
        return "master-template";
    }

    @GetMapping("/edit")
    public String getEditPage(HttpServletRequest request, Model model) {
        modifyModel(request, model);

        model.addAttribute("bodyContent", "edit");
        return "master-template";
    }

    // TODO: 02.3.2021 -> implementiraj mailto funkcija i dovrsi go metodov
    // za profilot na korisnikot, da ima opcii za prakjanje email ili brisenje na nekoj zacuvan oglas
    @GetMapping("/person_buttons/{id}")
    public String adEmailOrDelete(@PathVariable Long id,
                                  @RequestParam String personButton,
                                  HttpServletRequest request) {
        if(personButton.equals("delete")) this.adService.delete(request.getRemoteUser(), id);
        //else // email...
        return "redirect:/profile";
    }


    // TODO: 02.3.2021 -> posle implementiranje na prebaruvanje i pregled  na profili na kompanii, dovrsi go metodov
    // za profilot na kompanijata, koga ke bide otvoren od strana na korisnikot
    @GetMapping("/company_buttons_front/{id}")
    public String adInterestedOrEmail(@PathVariable Long id,
                                      @RequestParam String personButton,
                                      HttpServletRequest request) {
        if(personButton.equals("interested")) this.adService.save(request.getRemoteUser(), id);
        //else // email...
        return "redirect:/profile";
    }


    // TODO: 02.3.2021 -> kreiraj strana za edit na oglas, implementiraj ja funkcionalnosta i dovrsi go metodov
    // za profilot na kompanijata, da menadzira so svoite oglasi (da gi brise ili ureduva)
    @GetMapping("/company_buttons_back/{id}")
    public String adEditOrDelete(@PathVariable Long id,
                                 @RequestParam String otherButton) {
        if(otherButton.equals("delete")) this.adService.delete(id);
        //else // edit...
        return "redirect:/profile";
    }


    @PostMapping("/edit")
    public String postEditPage(@RequestParam(required = false) String fullName,
                               @RequestParam(required = false) String bio,
                               @RequestParam(required = false) String accountRole,
                               @RequestParam(required = false) MultipartFile profilePicture,
                               HttpServletRequest request) throws IOException {
        if(!profilePicture.isEmpty()) {
            File picture_target = new File(targetFolderImagePPPath + request.getRemoteUser() + "." + profilePicture.getOriginalFilename().split("\\.")[1]);

            if(picture_target.exists()) {
                picture_target.delete();
            }

            profilePicture.transferTo(picture_target);

            this.userService.update(request.getRemoteUser(), fullName, bio, accountRole, profilePicture, "../customProfilePictures/" + picture_target.getName());

            return "redirect:/profile";
        }

        this.userService.update(request.getRemoteUser(), fullName, bio, accountRole, profilePicture, "../customProfilePictures/");

        return "redirect:/profile";
    }

    // ==================================================== //

    public void modifyModel(HttpServletRequest request, Model model) {
        User user = this.userService.getUserInstanceByUUID(request.getRemoteUser());

        if (user instanceof Person && user.getRole() == Role.ROLE_ADMIN) {
            model.addAttribute("isAdmin", true);
            model.addAttribute("username", request.getRemoteUser());
        }
        else if(user instanceof Person) {
            model.addAttribute("isPerson", true);
            model.addAttribute("username", request.getRemoteUser());
            model.addAttribute("personAds", this.adService.getAdsByPersonUsername(user.getUsername()));
        }
        else  {
            model.addAttribute("isCompany", true);
            model.addAttribute("username", user.getName());
            model.addAttribute("companyId", ((Company) user).getId());
            model.addAttribute("companyAds", this.adService.getAdsByCompanyId(((Company) user).getId()));
        }

        model.addAttribute("bio", user.getBio());
        model.addAttribute("role", user.getAccountRole());
        model.addAttribute("imgsrc", user.getImageSourceUrl());
        model.addAttribute("email", user.getEmail());
    }

}
