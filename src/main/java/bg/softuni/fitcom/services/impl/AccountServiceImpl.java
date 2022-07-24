package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.exceptions.TokenExpiredException;
import bg.softuni.fitcom.models.entities.AccountEntity;
import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.entities.TokenEntity;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.service.AccountRegisterServiceModel;
import bg.softuni.fitcom.repositories.AccountRepository;
import bg.softuni.fitcom.repositories.RoleRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import bg.softuni.fitcom.services.AccountService;
import bg.softuni.fitcom.repositories.TokenRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final TokenRepository verificationTokenRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public AccountServiceImpl(TokenRepository verificationTokenRepository, AccountRepository accountRepository, UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AccountRegisterServiceModel create(AccountRegisterServiceModel serviceModel) {
        AccountEntity accountEntity = toEntity(serviceModel);
        this.accountRepository.save(accountEntity);
        return serviceModel;
    }

    private AccountEntity toEntity(AccountRegisterServiceModel serviceModel) {
        List<RoleEntity> roleEntities = serviceModel.getRoles() == null
                ? new ArrayList<>()
                : serviceModel.getRoles()
                .stream()
                .map(r -> this.roleRepository.findByRole(r)
                        .orElseThrow(() -> new ResourceNotFoundException(r + " is not a valid role!")))
                .collect(Collectors.toList());

        roleEntities.add(this.roleRepository.findByRole(RoleEnum.USER).get());

        return this.modelMapper.map(serviceModel, AccountEntity.class)
                .setRoles(roleEntities);
    }

    @Override
    public void createVerificationToken(AccountRegisterServiceModel serviceModel, String token) {
        TokenEntity verificationToken = new TokenEntity()
                .setToken(token)
                .setExpiryDate()
                .setEmail(serviceModel.getEmail());

        this.verificationTokenRepository.save(verificationToken);
    }

    @Override
    @Transactional
    public void confirmAccount(String token) {
        TokenEntity verificationToken = this.verificationTokenRepository.findByToken(token).get();

        if (verificationToken.getExpiryDate().before(new Date())) {
            throw new TokenExpiredException();
        }

        AccountEntity accountEntity = this.accountRepository.findByEmail(verificationToken.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Account with email " +  verificationToken.getEmail() + " does not exist!"));

        UserEntity userEntity = this.modelMapper.map(accountEntity, UserEntity.class);
        userEntity.setId(0);
        this.userRepository.save(userEntity);

        this.accountRepository.delete(accountEntity);
        this.verificationTokenRepository.delete(verificationToken);
    }
}
