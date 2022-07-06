package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.service.ResetTokenServiceModel;

public interface ResetTokenService {
    ResetTokenServiceModel getToken(String token);

    void deleteToken(long id);
}
