package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.models.entities.ResetTokenEntity;
import bg.softuni.fitcom.models.service.PasswordServiceModel;
import bg.softuni.fitcom.repositories.ResetTokenRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import bg.softuni.fitcom.services.PasswordService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final ResetTokenRepository resetTokenRepository;
    private final UserRepository userRepository;

    public PasswordServiceImpl(ResetTokenRepository resetTokenRepository, UserRepository userRepository) {
        this.resetTokenRepository = resetTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createResetToken(PasswordServiceModel password, String token) {
        ResetTokenEntity resetTokenEntity = new ResetTokenEntity()
                .setToken(token)
                .setEmail(password.getEmail())
                .setExpiryDate();

        this.resetTokenRepository.save(resetTokenEntity);
    }

    @Override
    public void update(String password, String email) {
        this.userRepository.updatePassword(password, email);
    }
}
