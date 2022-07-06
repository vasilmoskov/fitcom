package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.service.PasswordServiceModel;

public interface PasswordService {
    void createResetToken(PasswordServiceModel password, String token);

    void update(String password, String email);
}
