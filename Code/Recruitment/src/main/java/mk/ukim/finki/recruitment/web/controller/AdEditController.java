package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.service.AdService;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/adedit")
public class AdEditController {

    private static boolean feedFlag = false;
    private static boolean profileFlag = false;

    private UserService userService;
    private AdService adService;

    public AdEditController(UserService userService, AdService adService) {
        this.userService = userService;
        this.adService = adService;
    }

    @GetMapping
    public String getAdEditPage(@RequestParam String adId,
                                @RequestParam String header,
                                @RequestParam String body,
                                @RequestParam(required = false) String feedFlag,
                                @RequestParam(required = false) String profileFlag,
                                @RequestParam String companyId,
                                HttpServletRequest request,
                                Model model) {
        modifyModel(adId, header, body, feedFlag, profileFlag, companyId, request, model);

        model.addAttribute("bodyContent", "adEdit");
        return "master-template";
    }

    @PostMapping
    public String postAdEditPage(@RequestParam String adId,
                                 @RequestParam String header,
                                 @RequestParam String body) {
        this.adService.updateAd(Long.parseLong(adId), header, body);

        if(AdEditController.feedFlag) return "redirect:/feed";
        else return "redirect:/profile";
    }

    // ==================================================== //

    public void modifyModel(String adId, String header, String body, String feedFlag, String profileFlag, String companyId,
                            HttpServletRequest request, Model model) {
        model.addAttribute("username", this.userService.getUserInstanceByUUID(request.getRemoteUser()).getName());

        model.addAttribute("adId", adId);
        model.addAttribute("header", header);
        model.addAttribute("body", body);
        if(feedFlag != null && !feedFlag.isEmpty()) {
            model.addAttribute("feedFlag", true);
            AdEditController.feedFlag = true;
            AdEditController.profileFlag = false;
        }
        if(profileFlag != null && !profileFlag.isEmpty()) {
            model.addAttribute("profileFlag", true);
            AdEditController.feedFlag = false;
            AdEditController.profileFlag = true;
        }
        model.addAttribute("imageSourceUrl", this.userService.findCompanyById(companyId).getImageSourceUrl());

    }

}
