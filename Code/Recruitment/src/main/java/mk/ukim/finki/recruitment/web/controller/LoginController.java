package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String error,
                               @RequestParam(required = false) String success,
                               HttpServletRequest request,
                               Model model) {
        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
        }
        if(success != null && !success.isEmpty()) {
            model.addAttribute("accountCreated", true);
        }

        if(request.getRemoteUser() != null && this.userService.findPersonByUsername(request.getRemoteUser())) {
            model.addAttribute("username", request.getRemoteUser());
        }
        else if(request.getRemoteUser() != null) {
            Company company = this.userService.findCompanyById(request.getRemoteUser());
            model.addAttribute("username", company.getName());
        }

        model.addAttribute("bodyContent", "login");
        return "master-template";
    }

}
