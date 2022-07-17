package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.TokenEntity;
import bg.softuni.fitcom.models.service.TokenServiceModel;
import bg.softuni.fitcom.repositories.TokenRepository;
import bg.softuni.fitcom.services.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;

    public TokenServiceImpl(TokenRepository tokenRepository, ModelMapper modelMapper) {
        this.tokenRepository = tokenRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TokenServiceModel getToken(String token) {
        TokenEntity resetTokenEntity = this.tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("No such token!"));

        return modelMapper.map(resetTokenEntity, TokenServiceModel.class);
    }

    @Override
    public void deleteToken(long id) {
        this.tokenRepository.deleteById(id);
    }
}
