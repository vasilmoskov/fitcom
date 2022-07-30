package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.entities.AccountEntity;
import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.TokenEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.service.AccountRegisterServiceModel;
import bg.softuni.fitcom.repositories.AccountRepository;
import bg.softuni.fitcom.repositories.RoleRepository;
import bg.softuni.fitcom.repositories.TokenRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import bg.softuni.fitcom.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final int TEST_AGE = 52;
    private static final String TEST_EMAIL = "john.doe@mail.com";
    private static final String TEST_PASSWORD = "secret";

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    private AccountService serviceToTest;

    @BeforeEach
    void init() {
        serviceToTest = new AccountServiceImpl(tokenRepository, accountRepository,userRepository,
                roleRepository, modelMapper);
    }

    @Test
    public void testCreate() {
        AccountRegisterServiceModel serviceModel = getAccountRegisterServiceModel();
        AccountEntity accountEntity = getAccountEntity();
        RoleEntity roleUser = new RoleEntity().setRole(RoleEnum.USER);
        RoleEntity roleTrainer = new RoleEntity().setRole(RoleEnum.TRAINER);

        when(roleRepository.findByRole(RoleEnum.USER)).thenReturn(Optional.ofNullable(roleUser));
        when(roleRepository.findByRole(RoleEnum.TRAINER)).thenReturn(Optional.ofNullable(roleTrainer));
        when(modelMapper.map(serviceModel, AccountEntity.class)).thenReturn(accountEntity);
        when(accountRepository.save(accountEntity)).thenReturn(accountEntity);

        AccountRegisterServiceModel created = serviceToTest.create(serviceModel);

        verify(accountRepository, times(1)).save(accountEntity);
        assertEquals(created.getFirstName(), TEST_FIRST_NAME);
        assertEquals(created.getLastName(), TEST_LAST_NAME);
        assertEquals(created.getAge(), TEST_AGE);
        assertEquals(created.getEmail(), TEST_EMAIL);
        assertEquals(created.getPassword(), TEST_PASSWORD);
    }

    @Test
    public void testCreateVerificationToken() {
        AccountRegisterServiceModel serviceModel = getAccountRegisterServiceModel();
        String token = UUID.randomUUID().toString();

        serviceToTest.createVerificationToken(serviceModel, token);

        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    public void testConfirmAccount() {
        String token = UUID.randomUUID().toString();

        TokenEntity verificationToken = getVerificationToken(token);
        AccountEntity accountEntity = getAccountEntity();
        UserEntity userEntity = getUserEntity();

        when(tokenRepository.findByToken(token)).thenReturn(Optional.ofNullable(verificationToken));
        when(accountRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.ofNullable(accountEntity));
        when(modelMapper.map(accountEntity, UserEntity.class)).thenReturn(userEntity);

        serviceToTest.confirmAccount(token);

        verify(userRepository, times(1)).save(userEntity);
        verify(accountRepository, times(1)).delete(accountEntity);
        verify(tokenRepository, times(1)).delete(verificationToken);
    }

    private TokenEntity getVerificationToken(String token) {
        return new TokenEntity()
                .setToken(token)
                .setExpiryDate()
                .setEmail(TEST_EMAIL);
    }

    private UserEntity getUserEntity() {
        return new UserEntity()
                .setFirstName(TEST_FIRST_NAME)
                .setLastName(TEST_LAST_NAME)
                .setAge(TEST_AGE)
                .setEmail(TEST_EMAIL)
                .setPassword(TEST_PASSWORD);
    }

    private AccountRegisterServiceModel getAccountRegisterServiceModel() {
        return new AccountRegisterServiceModel()
                .setFirstName(TEST_FIRST_NAME)
                .setLastName(TEST_LAST_NAME)
                .setAge(TEST_AGE)
                .setEmail(TEST_EMAIL)
                .setPassword(TEST_PASSWORD)
                .setRoles(List.of(RoleEnum.TRAINER));
    }

    private AccountEntity getAccountEntity() {
        return new AccountEntity()
                .setFirstName(TEST_FIRST_NAME)
                .setLastName(TEST_LAST_NAME)
                .setAge(TEST_AGE)
                .setEmail(TEST_EMAIL)
                .setPassword(TEST_PASSWORD);
    }
}