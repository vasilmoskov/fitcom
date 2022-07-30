package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.entities.TokenEntity;
import bg.softuni.fitcom.models.service.PasswordServiceModel;
import bg.softuni.fitcom.repositories.TokenRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import bg.softuni.fitcom.services.impl.PasswordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PasswordServiceTest {
    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    private PasswordService serviceToTest;

    @BeforeEach
    void setup() {
        serviceToTest = new PasswordServiceImpl(tokenRepository, userRepository);
    }

    @Test
    void testCreateResetToken() {
        PasswordServiceModel password = new PasswordServiceModel().setEmail("pesh0@abv.bg");
        String token = UUID.randomUUID().toString();

        serviceToTest.createResetToken(password, token);

        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    void testUpdatePassword() {
        String password = "topsecret";
        String email = "pesh0@abv.bg";

        serviceToTest.update(password, email);

        verify(userRepository, times(1)).updatePassword(password, email);
    }

}
