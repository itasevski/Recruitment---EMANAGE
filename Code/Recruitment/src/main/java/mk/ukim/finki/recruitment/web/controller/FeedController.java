package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.model.Company;
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
        if(request.getRemoteUser() != null && this.userService.findPersonByUsername(request.getRemoteUser())) {
            model.addAttribute("username", request.getRemoteUser());
        }
        else if(request.getRemoteUser() != null) {
            Company company = this.userService.findCompanyById(request.getRemoteUser());
            model.addAttribute("username", company.getName());
        }

        model.addAttribute("feedSearchFlag", true);

        model.addAttribute("bodyContent", "feed");
        return "master-template";
    }

}
