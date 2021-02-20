package mk.ukim.finki.recruitment.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping
    public String getProfilePage(Model model) {
        model.addAttribute("bodyContent", "profile");
        return "master-template";
    }

    @PostMapping
    public String postProfilePage(Model model) {
        return getProfilePage(model);
    }

    @GetMapping("/edit")
    public String getEditPage(Model model) {
        model.addAttribute("bodyContent", "edit");
        return "master-template";
    }

}
