package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.ResetTokenEntity;
import bg.softuni.fitcom.models.service.ResetTokenServiceModel;
import bg.softuni.fitcom.repositories.ResetTokenRepository;
import bg.softuni.fitcom.services.ResetTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ResetTokenServiceImpl implements ResetTokenService {
    private final ResetTokenRepository resetTokenRepository;
    private final ModelMapper modelMapper;

    public ResetTokenServiceImpl(ResetTokenRepository resetTokenRepository, ModelMapper modelMapper) {
        this.resetTokenRepository = resetTokenRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResetTokenServiceModel getToken(String token) {
        ResetTokenEntity resetTokenEntity = this.resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("No such token!"));

        return modelMapper.map(resetTokenEntity, ResetTokenServiceModel.class);
    }

    @Override
    public void deleteToken(long id) {
        this.resetTokenRepository.deleteById(id);
    }
}
