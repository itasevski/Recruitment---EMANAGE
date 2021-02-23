package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = {"/", "/home"})
public class HomeController {

    private UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(HttpServletRequest request, Model model) {
        if(request.getRemoteUser() != null && this.userService.findPersonByUsername(request.getRemoteUser())) {
            model.addAttribute("isPerson", true);
            model.addAttribute("username", request.getRemoteUser());
            model.addAttribute("isCompany", false);
        }
        else if(request.getRemoteUser() != null) {
            Company company = this.userService.findCompanyById(request.getRemoteUser());
            model.addAttribute("isPerson", false);
            model.addAttribute("isCompany", true);
            model.addAttribute("username", company.getName());
        }

        return "home";
    }

}
