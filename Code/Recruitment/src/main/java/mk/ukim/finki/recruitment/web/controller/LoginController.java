package mk.ukim.finki.recruitment.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String error,
                               @RequestParam(required = false) String success,
                               Model model) {
        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
        }
        if(success != null && !success.isEmpty()) {
            model.addAttribute("accountCreated", true);
        }
        model.addAttribute("bodyContent", "login");
        return "master-template";
    }

}
