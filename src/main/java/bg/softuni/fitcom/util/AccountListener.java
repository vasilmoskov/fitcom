package bg.softuni.fitcom.util;

import bg.softuni.fitcom.models.service.AccountRegisterServiceModel;
import bg.softuni.fitcom.services.AccountService;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountListener implements ApplicationListener<OnCreateAccountEvent> {

    private String serverUrl = "http://localhost:8080/";
    private final JavaMailSender mailSender;
    private final AccountService accountService;

    public AccountListener(JavaMailSender mailSender, AccountService accountService) {
        this.mailSender = mailSender;
        this.accountService = accountService;
    }

    @Override
    public void onApplicationEvent(OnCreateAccountEvent event) {
        AccountRegisterServiceModel serviceModel = event.getServiceModel();
        String token = UUID.randomUUID().toString();
        accountService.createVerificationToken(serviceModel, token);

        String recipientAddress = serviceModel.getEmail();
        String subject = "Account Confirmation";
        String confirmationUrl = event.getAppUrl() + "register/confirm?token=" + token;
        String message = "Please confirm:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + serverUrl + confirmationUrl);
        mailSender.send(email);
    }
}
