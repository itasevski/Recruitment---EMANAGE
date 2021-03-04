package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.model.Person;
import mk.ukim.finki.recruitment.model.User;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/profiles")
public class ProfileListController {

    private UserService userService;

    public ProfileListController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getProfileListPage(HttpServletRequest request, Model model) {
        User user = this.userService.getUserInstanceByUUID(request.getRemoteUser());

        model.addAttribute("username",
                user instanceof Person
                        ? user.getUsername()
                        : user.getName());

        List<User> users = this.userService.getAllUsers();

        if(users.size() == 0) model.addAttribute("noProfilesExist", true);

        model.addAttribute("profiles", users);

        model.addAttribute("bodyContent", "profileList");
        return "master-template";
    }

    @PostMapping
    public String filterProfileListPage(@RequestParam String profileSelect,
                                  HttpServletRequest request, Model model) {
        User user = this.userService.getUserInstanceByUUID(request.getRemoteUser());

        model.addAttribute("username",
                user instanceof Person
                        ? user.getUsername()
                        : user.getName());

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

        User user = this.userService.getUserInstanceByUUID(request.getRemoteUser());

        model.addAttribute("username",
                user instanceof Person
                        ? user.getUsername()
                        : user.getName());

        List<User> users = this.userService.getUsersByQueryString(queryString);

        if(users.size() == 0) model.addAttribute("notFoundByQuery", true);

        model.addAttribute("profiles", users);

        model.addAttribute("bodyContent", "profileList");
        return "master-template";
    }

}
