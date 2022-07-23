package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.binding.AccountRegisterBindingModel;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.service.AccountRegisterServiceModel;
import bg.softuni.fitcom.models.service.CloudinaryImageServiceModel;
import bg.softuni.fitcom.services.AccountService;
import bg.softuni.fitcom.services.CloudinaryService;
import bg.softuni.fitcom.util.OnCreateAccountEvent;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/register")
public class AccountController {
    private final AccountService accountService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final LocaleResolver localeResolver;
    private final PasswordEncoder passwordEncoder;

    public AccountController(AccountService accountService, CloudinaryService cloudinaryService, ModelMapper modelMapper,
                             ApplicationEventPublisher eventPublisher, LocaleResolver localeResolver, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
        this.localeResolver = localeResolver;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getRegister(Model model, Authentication auth) {
        if (auth != null) {
            return "redirect:/";
        }

        model.addAttribute("roles", List.of(RoleEnum.NUTRITIONIST, RoleEnum.TRAINER));
        return "register";
    }

    @ModelAttribute("registerModel")
    public AccountRegisterBindingModel registerModel() {
        return new AccountRegisterBindingModel();
    }

    @PostMapping
    public String doRegister(@Valid AccountRegisterBindingModel bindingModel, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerModel", bindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.registerModel", bindingResult);

            return "redirect:/register";
        }

        AccountRegisterServiceModel serviceModel = this.modelMapper
                .map(bindingModel, AccountRegisterServiceModel.class)
                .setPassword(passwordEncoder.encode(bindingModel.getPassword()));

        if (!bindingModel.getAvatar().isEmpty()) {
            CloudinaryImageServiceModel upload = cloudinaryService.upload(bindingModel.getAvatar());

            serviceModel.setPictureUrl(upload.getUrl())
                    .setPicturePublicId(upload.getPublicId());
        } else {
            serviceModel.setPictureUrl(
                    "https://res.cloudinary.com/dilbpiicv/image/upload/v1654441524/default_avatar_wmftdp.webp");
        }

        this.accountService.create(serviceModel);
        this.eventPublisher.publishEvent(new OnCreateAccountEvent(serviceModel, "",
                localeResolver.resolveLocale(request)));

        return "email-sent";
    }

    @GetMapping("/confirm")
    public String confirmAccount(@RequestParam("token") String token) {
        this.accountService.confirmAccount(token);
        return "registration-confirmed";
    }
}
