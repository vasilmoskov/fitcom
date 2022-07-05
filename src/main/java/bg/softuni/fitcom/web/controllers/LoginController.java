package bg.softuni.fitcom.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLoginPage(Authentication auth) {
        if (auth != null) {
            return "redirect:/";
        }

        return "login";
    }

    @PostMapping("/login-error")
    public String failedLogin(@ModelAttribute("email") String email, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("bad_credentials", true);
        redirectAttributes.addFlashAttribute("email", email);

        return "redirect:/login";
    }
}
