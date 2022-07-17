package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.models.entities.TokenEntity;
import bg.softuni.fitcom.models.service.PasswordServiceModel;
import bg.softuni.fitcom.repositories.TokenRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import bg.softuni.fitcom.services.PasswordService;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public PasswordServiceImpl(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createResetToken(PasswordServiceModel password, String token) {
        TokenEntity resetTokenEntity = new TokenEntity()
                .setToken(token)
                .setEmail(password.getEmail())
                .setExpiryDate();

        this.tokenRepository.save(resetTokenEntity);
    }

    @Override
    public void update(String password, String email) {
        this.userRepository.updatePassword(password, email);
    }
}
