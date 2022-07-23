package bg.softuni.fitcom.util;

import bg.softuni.fitcom.models.service.AccountRegisterServiceModel;
import bg.softuni.fitcom.services.AccountService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@Component
public class AccountListener implements ApplicationListener<OnCreateAccountEvent> {

    private String serverUrl = "http://localhost:8080/";

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MessageSource messageSource;
    private final Context context;

    private final AccountService accountService;

    public AccountListener(JavaMailSender mailSender, TemplateEngine templateEngine, MessageSource messageSource, AccountService accountService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
        this.accountService = accountService;
        this.context = new Context();
    }

    @Override
    public void onApplicationEvent(OnCreateAccountEvent event) {
        AccountRegisterServiceModel serviceModel = event.getServiceModel();
        String token = UUID.randomUUID().toString();
        accountService.createVerificationToken(serviceModel, token);

        String recipientAddress = serviceModel.getEmail();
        String user = serviceModel.getFirstName() + " " + serviceModel.getLastName();
        String confirmationUrl = event.getAppUrl() + "register/confirm?token=" + token;

        context.setLocale(event.getLocale());
        context.setVariable("user", user);
        context.setVariable("registration_link", serverUrl + confirmationUrl);
        String subject = messageSource.getMessage("registration_subject", new Object[0], event.getLocale());
        String text = templateEngine.process("email/registration", context);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(text);
        mailSender.send(email);
    }
}
