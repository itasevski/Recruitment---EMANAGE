package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.model.Ad;
import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.model.Person;
import mk.ukim.finki.recruitment.model.User;
import mk.ukim.finki.recruitment.model.enumerations.Role;
import mk.ukim.finki.recruitment.model.exceptions.AdAlreadySavedException;
import mk.ukim.finki.recruitment.service.AdService;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public String getFeedPage(@RequestParam(required = false) String error,
                              HttpServletRequest request, Model model) {
        if(error != null && !error.isEmpty()) model.addAttribute("hasError", true);

        modifyModel(request, model);

        model.addAttribute("bodyContent", "feed");
        return "master-template";
    }

    @GetMapping("/person/{id}")
    public String adInterestedOrEmail(@PathVariable Long id,
                                      @RequestParam String userButton,
                                      HttpServletRequest request) {
        if(userButton.equals("interested")) {
            try {
                this.adService.save(request.getRemoteUser(), id);
            }
            catch (AdAlreadySavedException exception) {
                return "redirect:/feed?error=" + exception.getMessage();
            }
        }
        else {
            Company adOwner = this.adService.getAdOwner(id);
            return "redirect:/email?address=" + adOwner.getEmail() + "&subject=" + this.adService.findById(id).getHeader()
                    + "&feedFlag=true";
        }

        return "redirect:/profile";
    }

    @GetMapping("/other/{id}")
    public String adEditOrDelete(@PathVariable Long id,
                                 @RequestParam String otherButton) {
        if(otherButton.equals("delete")) this.adService.delete(id);
        else {
            Ad ad = this.adService.findById(id);
            return "redirect:/adedit?adId=" + ad.getId() + "&header=" + ad.getHeader() + "&body=" + ad.getBody() + "&feedFlag=true"
                    + "&companyId=" + ad.getCompany().getId();
        }

        return "redirect:/feed";
    }

    @PostMapping("/sort")
    public String postSort(@RequestParam String sortSelect,
                           HttpServletRequest request, Model model) {
        modifyModelWithSort(sortSelect, request, model);

        model.addAttribute("bodyContent", "feed");
        return "master-template";
    }

    @PostMapping("/search")
    public String postSearch(@RequestParam(required = false) String queryString,
                             HttpServletRequest request, Model model) {
        if(queryString == null || queryString.isEmpty()) return "redirect:/feed";

        modifyModelWithSearch(queryString, request, model);

        model.addAttribute("bodyContent", "feed");
        return "master-template";
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

        List<Ad> ads = this.adService.getAllAds();

        if(ads.size() == 0) model.addAttribute("noAdsPosted", true);

        model.addAttribute("allAds", ads);
        model.addAttribute("numActiveUsers", this.userService.getActiveUsers());
    }

    public void modifyModelWithSort(String sortCriteria, HttpServletRequest request, Model model) {
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
        model.addAttribute(sortCriteria, true);

        List<Ad> ads = this.adService.getSortedAds(sortCriteria);

        if(ads.size() == 0) model.addAttribute("noAdsPosted", true);

        model.addAttribute("allAds", ads);
        model.addAttribute("numActiveUsers", this.userService.getActiveUsers());
    }

    public void modifyModelWithSearch(String queryString, HttpServletRequest request, Model model) {
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

        List<Ad> ads = this.adService.getAdsByQueryString(queryString);

        if(ads.size() == 0) model.addAttribute("notFoundByQuery", true);

        model.addAttribute("allAds", ads);
        model.addAttribute("numActiveUsers", this.userService.getActiveUsers());
    }

}
