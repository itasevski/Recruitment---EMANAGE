package mk.ukim.finki.recruitment.web.controller;

import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/email")
public class EmailController {

    private static boolean feedFlag = false;
    private static boolean profileFlag = false;

    private UserService userService;

    public EmailController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getEmailPage(@RequestParam String address,
                               @RequestParam String subject,
                               @RequestParam(required = false) String feedFlag,
                               @RequestParam(required = false) String profileFlag,
                               HttpServletRequest request,
                               Model model) {
        modifyModel(address, subject, feedFlag, profileFlag, request, model);

        model.addAttribute("bodyContent", "email");
        return "master-template";
    }

    @PostMapping
    public String postEmailPage(@RequestParam String address,
                                @RequestParam String subject,
                                @RequestParam String message) throws IOException, InterruptedException {
        String uriString = "mailto:" + address + "?subject=" + encodeString(subject) + "&body=" + encodeString(message);

        String cmd = "cmd.exe /c start \"\" \"" + uriString + "\"";
        Runtime.getRuntime().exec(cmd);

        Thread.sleep(5000);

        if(EmailController.feedFlag) return "redirect:/feed";
        else return "redirect:/profile";
    }

    // ==================================================== //

    public String encodeString(String in) {
        StringBuilder out = new StringBuilder();
        for (char ch : in.toCharArray()) {
            out.append(Character.isLetterOrDigit(ch) ? ch : String.format("%%%02X", (int) ch));
        }
        return out.toString();
    }

    public void modifyModel(String address, String subject, String feedFlag, String profileFlag,
                            HttpServletRequest request, Model model) {
        model.addAttribute("username", this.userService.getUserInstanceByUUID(request.getRemoteUser()).getUsername());
        model.addAttribute("isPerson", true);
        model.addAttribute("emailAddress", address);
        model.addAttribute("adSubject", subject);
        if(feedFlag != null && !feedFlag.isEmpty()) {
            model.addAttribute("feedFlag", true);
            EmailController.feedFlag = true;
            EmailController.profileFlag = false;
        }
        if(profileFlag != null && !profileFlag.isEmpty()) {
            model.addAttribute("profileFlag", true);
            EmailController.feedFlag = false;
            EmailController.profileFlag = true;
        }
    }

}
