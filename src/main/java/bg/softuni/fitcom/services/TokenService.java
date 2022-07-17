package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.service.TokenServiceModel;

public interface TokenService {
    TokenServiceModel getToken(String token);

    void deleteToken(long id);
}
