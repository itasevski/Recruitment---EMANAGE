package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.model.Person;
import mk.ukim.finki.recruitment.model.User;
import mk.ukim.finki.recruitment.model.enumerations.Role;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/feed")
public class FeedController {

    private UserService userService;

    public FeedController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getFeedPage(HttpServletRequest request, Model model) {
        if(request.getRemoteUser() != null) {
            User user = this.userService.getUserInstanceByUUID(request.getRemoteUser());

            if (user instanceof Person && user.getRole() == Role.ROLE_ADMIN) {
                model.addAttribute("isAdmin", true);
                model.addAttribute("deletePrivilege", true);
            }
            else if (user instanceof Person) {
//            model.addAttribute("username", user.getUsername());
                model.addAttribute("isPerson", true);
            } else {
//            model.addAttribute("username", user.getName());
                model.addAttribute("isCompany", true);
                model.addAttribute("deletePrivilege", true);
//            model.addAttribute("companyId", ((Company) user).getId());
            }
        }

//        model.addAttribute("imgsrc", user.getImageSourceUrl());
        model.addAttribute("feedSearchFlag", true);

        model.addAttribute("bodyContent", "feed");
        return "master-template";
    }

}
