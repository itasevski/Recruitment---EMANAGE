package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getProfilePage(HttpServletRequest request, Model model) {
        if(request.getRemoteUser() != null && this.userService.findPersonByUsername(request.getRemoteUser())) {
            model.addAttribute("username", request.getRemoteUser());
        }
        else if(request.getRemoteUser() != null) {
            Company company = this.userService.findCompanyById(request.getRemoteUser());
            model.addAttribute("username", company.getName());
        }

        model.addAttribute("bodyContent", "profile");
        return "master-template";
    }

    @PostMapping
    public String postProfilePage(Model model) {
        model.addAttribute("bodyContent", "profile");
        return "master-template";
    }

    @GetMapping("/edit")
    public String getEditPage(Model model) {
        model.addAttribute("bodyContent", "edit");
        return "master-template";
    }

}
