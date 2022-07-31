package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.exceptions.TokenExpiredException;
import bg.softuni.fitcom.models.service.PasswordServiceModel;
import bg.softuni.fitcom.models.service.TokenServiceModel;
import bg.softuni.fitcom.services.PasswordService;
import bg.softuni.fitcom.services.TokenService;
import bg.softuni.fitcom.util.OnPasswordResetEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class PasswordController {

    private final ApplicationEventPublisher eventPublisher;
    private final TokenService resetTokenService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordService passwordService;

    public PasswordController(ApplicationEventPublisher eventPublisher, TokenService resetTokenService,
                              PasswordEncoder passwordEncoder, PasswordService passwordService) {
        this.eventPublisher = eventPublisher;
        this.resetTokenService = resetTokenService;
        this.passwordEncoder = passwordEncoder;
        this.passwordService = passwordService;
    }

    @GetMapping("/password")
    public String getPasswordReset(@ModelAttribute("password") PasswordServiceModel password) {
        return "password";
    }

    @PostMapping("/password")
    public String sendEmailToReset(@ModelAttribute("password") PasswordServiceModel password) {
        eventPublisher.publishEvent(new OnPasswordResetEvent(password, ""));
        return "email-sent";
    }

    @ModelAttribute("passwordModel")
    public PasswordServiceModel passwordModel() {
        return new PasswordServiceModel();
    }

    @GetMapping("/password-reset")
    public String getNewPassword(@RequestParam("token") String token, Model model) {
        ((PasswordServiceModel) model.getAttribute("passwordModel"))
                .setToken(token);

        return "reset-password";
    }

    @PostMapping("/password-reset")
    public String saveNewPassword(@RequestParam("token") String token,
                                  @Valid PasswordServiceModel password,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("passwordModel", password);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.passwordModel", bindingResult);
            return "redirect:/password-reset?token=" + token;
        }

        TokenServiceModel tokenServiceModel = this.resetTokenService.getToken(token);
        this.resetTokenService.deleteToken(tokenServiceModel.getId());

        if (tokenServiceModel.getExpiryDate().before(new Date())) {
            throw new TokenExpiredException();
        } else {
            password.setPassword(passwordEncoder.encode(password.getPassword()));
            this.passwordService.update(password.getPassword(), tokenServiceModel.getEmail());
            return "redirect:/login";
        }
    }
}
