package mk.ukim.finki.recruitment.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/feed")
public class FeedController {

    @GetMapping
    public String getFeedPage(Model model) {
        model.addAttribute("bodyContent", "feed");
        return "master-template";
    }

}
