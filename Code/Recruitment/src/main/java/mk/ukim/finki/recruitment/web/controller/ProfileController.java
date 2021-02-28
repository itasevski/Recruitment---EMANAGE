package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.model.Person;
import mk.ukim.finki.recruitment.model.User;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    public static final String resourcesFolderImagePPPath = "C:\\Users\\User\\Desktop\\Recruitment---EMANAGE\\Code\\Recruitment\\target\\classes\\static\\customProfilePictures\\";

    private UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getProfilePage(HttpServletRequest request, Model model) {
        modifyModel(request, model);

        model.addAttribute("bodyContent", "profile");
        return "master-template";
    }

    @GetMapping("/edit")
    public String getEditPage(HttpServletRequest request, Model model) {
        modifyModel(request, model);

        model.addAttribute("bodyContent", "edit");
        return "master-template";
    }

    @PostMapping("/edit")
    public String postEditPage(@RequestParam(required = false) String fullName,
                               @RequestParam(required = false) String bio,
                               @RequestParam(required = false) String accountRole,
                               @RequestParam(required = false) MultipartFile profilePicture,
                               HttpServletRequest request) throws IOException {
        if(!profilePicture.isEmpty()) {
            File picture_resources = new File(resourcesFolderImagePPPath + request.getRemoteUser() + "." + profilePicture.getOriginalFilename().split("\\.")[1]);

            if(picture_resources.exists()) {
                picture_resources.delete();
            }

            profilePicture.transferTo(picture_resources);

            this.userService.update(request.getRemoteUser(), fullName, bio, accountRole, profilePicture, "../customProfilePictures/" + picture_resources.getName());

            return "redirect:/profile";
        }

        this.userService.update(request.getRemoteUser(), fullName, bio, accountRole, profilePicture, "../customProfilePictures/");

        return "redirect:/profile";
    }

    // ==================================================== //

    public void modifyModel(HttpServletRequest request, Model model) {
        User user = this.userService.getUserInstanceByUUID(request.getRemoteUser());

        if(user instanceof Person) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("isPerson", true);
        }
        else  {
            model.addAttribute("username", user.getName());
            model.addAttribute("isCompany", true);
            model.addAttribute("companyId", ((Company) user).getId());
        }

        model.addAttribute("bio", user.getBio());
        model.addAttribute("role", user.getAccountRole());
        model.addAttribute("imgsrc", user.getImageSourceUrl());
        model.addAttribute("email", user.getEmail());
    }

}
