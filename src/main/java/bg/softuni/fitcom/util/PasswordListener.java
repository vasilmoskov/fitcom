package bg.softuni.fitcom.util;

import bg.softuni.fitcom.models.service.PasswordServiceModel;
import bg.softuni.fitcom.services.PasswordService;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PasswordListener implements ApplicationListener<OnPasswordResetEvent> {
    private final String serverUrl = "http://localhost:8080/";

    private final JavaMailSender mailSender;
    private final PasswordService passwordService;

    public PasswordListener(JavaMailSender mailSender, PasswordService passwordService) {
        this.mailSender = mailSender;
        this.passwordService = passwordService;
    }

    @Override
    public void onApplicationEvent(OnPasswordResetEvent event) {
        PasswordServiceModel password = event.getPassword();
        String token = UUID.randomUUID().toString();
        this.passwordService.createResetToken(password, token);

        String recipientAddress = password.getEmail();
        String subject = "Reset Password";
        String message = "Reset Password";
        String confirmationUrl = event.getAppUrl() + "password-reset?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + serverUrl + confirmationUrl);
        this.mailSender.send(email);
    }
}
