package bg.softuni.fitcom.util;

import bg.softuni.fitcom.models.service.AccountRegisterServiceModel;
import org.springframework.context.ApplicationEvent;

public class OnCreateAccountEvent extends ApplicationEvent {
    private String appUrl;
    private AccountRegisterServiceModel serviceModel;

    public OnCreateAccountEvent(AccountRegisterServiceModel serviceModel, String appUrl) {
        super(serviceModel);
        this.serviceModel = serviceModel;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public AccountRegisterServiceModel getServiceModel() {
        return serviceModel;
    }
}
