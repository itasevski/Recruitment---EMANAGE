package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/results")
public class ResultsController {

    private UserService userService;

    public ResultsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getResultsPage(HttpServletRequest request, Model model) {
        model.addAttribute("username", this.userService.getUserInstanceByUUID(request.getRemoteUser()).getUsername());
        model.addAttribute("isPerson", true);

        model.addAttribute("bodyContent", "results");
        return "master-template";
    }

}
