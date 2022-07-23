package bg.softuni.fitcom.util;

import bg.softuni.fitcom.models.service.AccountRegisterServiceModel;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnCreateAccountEvent extends ApplicationEvent {
    private String appUrl;
    private AccountRegisterServiceModel serviceModel;
    private Locale locale;

    public OnCreateAccountEvent(AccountRegisterServiceModel serviceModel, String appUrl, Locale locale) {
        super(serviceModel);
        this.serviceModel = serviceModel;
        this.appUrl = appUrl;
        this.locale = locale;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public AccountRegisterServiceModel getServiceModel() {
        return serviceModel;
    }

    public Locale getLocale() {
        return locale;
    }
}
