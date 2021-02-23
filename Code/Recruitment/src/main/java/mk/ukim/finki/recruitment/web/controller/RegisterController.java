package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error,
                                  HttpServletRequest request,
                                  Model model) {
        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", error);
        }

        if(request.getRemoteUser() != null && this.userService.findPersonByUsername(request.getRemoteUser())) {
            model.addAttribute("username", request.getRemoteUser());
        }
        else if(request.getRemoteUser() != null) {
            Company company = this.userService.findCompanyById(request.getRemoteUser());
            model.addAttribute("username", company.getName());
        }

        model.addAttribute("bodyContent", "register");
        return "master-template";
    }

    @PostMapping
    public String postRegisterPage(@RequestParam String username,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String repeatedPassword,
                                   @RequestParam String userType) {
        try {
            this.userService.register(username, email, password, repeatedPassword, userType);
        }
        catch (RuntimeException exception) {
            return "redirect:/register?error=" + exception.getMessage();
        }
        return "redirect:/login?success=AccountSuccessfullyCreated";
    }

}
