package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.entities.TokenEntity;
import bg.softuni.fitcom.models.service.TokenServiceModel;
import bg.softuni.fitcom.repositories.TokenRepository;
import bg.softuni.fitcom.services.impl.TokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private ModelMapper modelMapper;

    private TokenService serviceToTest;

    @BeforeEach
    void setup() {
        serviceToTest = new TokenServiceImpl(tokenRepository, modelMapper);
    }

    @Test
    void testGetToken() {
        String token = UUID.randomUUID().toString();
        TokenEntity tokenEntity = createTokenEntity(token);
        TokenServiceModel tokenServiceModel = createTokenServiceModel(token);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.ofNullable(tokenEntity));
        when(modelMapper.map(tokenEntity, TokenServiceModel.class)).thenReturn(tokenServiceModel);

        TokenServiceModel actual = serviceToTest.getToken(token);

        assertEquals(tokenServiceModel, actual);
    }

    @Test
    void testDeleteToken() {
        serviceToTest.deleteToken(1);

        verify(tokenRepository, times(1)).deleteById(1L);
    }

    private TokenServiceModel createTokenServiceModel(String token) {
        return new TokenServiceModel()
                .setToken(token)
                .setEmail("pesho@abv.bg");
    }

    private TokenEntity createTokenEntity(String token) {
        return new TokenEntity()
                .setToken(token)
                .setEmail("pesho@abv.bg");
    }
}
