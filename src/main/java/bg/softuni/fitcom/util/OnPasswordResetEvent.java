package bg.softuni.fitcom.util;

import bg.softuni.fitcom.models.service.PasswordServiceModel;
import org.springframework.context.ApplicationEvent;

import javax.validation.Valid;

public class OnPasswordResetEvent extends ApplicationEvent {
    private String appUrl;
    private PasswordServiceModel password;

    public OnPasswordResetEvent(PasswordServiceModel password, String appUrl) {
        super(password);

        this.appUrl = appUrl;
        this.password = password;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public OnPasswordResetEvent setAppUrl(String appUrl) {
        this.appUrl = appUrl;
        return this;
    }

    public PasswordServiceModel getPassword() {
        return password;
    }

    public OnPasswordResetEvent setPassword(PasswordServiceModel password) {
        this.password = password;
        return this;
    }
}
