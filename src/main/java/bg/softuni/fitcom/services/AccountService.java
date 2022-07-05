package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.service.AccountRegisterServiceModel;

public interface AccountService {
    AccountRegisterServiceModel create(AccountRegisterServiceModel serviceModel);

    void createVerificationToken(AccountRegisterServiceModel serviceModel, String token);

    void confirmAccount(String token);
}
