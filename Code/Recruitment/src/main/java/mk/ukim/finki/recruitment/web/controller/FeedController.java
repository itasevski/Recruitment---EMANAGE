package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.model.Person;
import mk.ukim.finki.recruitment.model.User;
import mk.ukim.finki.recruitment.model.enumerations.Role;
import mk.ukim.finki.recruitment.service.AdService;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/feed")
public class FeedController {

    private UserService userService;
    private AdService adService;

    public FeedController(UserService userService, AdService adService) {
        this.userService = userService;
        this.adService = adService;
    }

    @GetMapping
    public String getFeedPage(HttpServletRequest request, Model model) {
        modifyModel(request, model);

        model.addAttribute("bodyContent", "feed");
        return "master-template";
    }

    // TODO: 01.3.2021 -> da se implementira ovoj metod, za kopcinjata "I'm interested" i "Send e-mail"
    @GetMapping("/person/{id}")
    public String adInterestedOrEmail(@PathVariable Long id,
                                      @RequestParam String userButton,
                                      HttpServletRequest request) {
        if(userButton.equals("interested")) {
            this.adService.save(request.getRemoteUser(), id);
        }
//        else {
//            // email...
//        }
        return "redirect:/profile";
    }

    // TODO: 01.3.2021 -> da se kreira edit stranica za editiranje na oglasite na kompanijata
    @GetMapping("/other/{id}")
    public String adEditOrDelete(@PathVariable Long id,
                                 @RequestParam String otherButton) {
        if(otherButton.equals("delete")) this.adService.delete(id);
        //else // edit...
        return "redirect:/feed";
    }

    @PostMapping
    public String postFeedPage(@RequestParam String header,
                               @RequestParam String body,
                               HttpServletRequest request) {
        this.adService.save(header, body, this.userService.findCompanyById(request.getRemoteUser()));
        return "redirect:/feed";
    }

    // ==================================================== //

    public void modifyModel(HttpServletRequest request, Model model) {
        if(request.getRemoteUser() != null) {
            User user = this.userService.getUserInstanceByUUID(request.getRemoteUser());

            if (user instanceof Person && user.getRole() == Role.ROLE_ADMIN) {
                model.addAttribute("isAdmin", true);
                model.addAttribute("username", request.getRemoteUser());
            }
            else if (user instanceof Person) {
                model.addAttribute("isPerson", true);
                model.addAttribute("username", request.getRemoteUser());
            } else {
                model.addAttribute("isCompany", true);
                model.addAttribute("username", user.getName());
            }
        }
        else {
            model.addAttribute("defaultProfilePicture", "../images/profilePictures/company-default.png");
        }

        model.addAttribute("feedSearchFlag", true);

        model.addAttribute("allAds", this.adService.getAllAds());
        model.addAttribute("numActiveUsers", this.userService.getActiveUsers());
    }

}
