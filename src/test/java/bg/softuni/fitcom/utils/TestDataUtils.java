package bg.softuni.fitcom.utils;

import bg.softuni.fitcom.models.entities.AccountEntity;
import bg.softuni.fitcom.models.entities.ExerciseEntity;
import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.TokenEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.user.FitcomUser;
import bg.softuni.fitcom.repositories.AccountRepository;
import bg.softuni.fitcom.repositories.ExerciseRepository;
import bg.softuni.fitcom.repositories.RoleRepository;
import bg.softuni.fitcom.repositories.TokenRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestDataUtils {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ExerciseRepository exerciseRepository;

    public void cleanUpDatabase() {
        tokenRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        exerciseRepository.deleteAll();
    }

    public FitcomUser getUser() {
        return new FitcomUser(
                "topsecret",
                "georgi@abv.bg",
                "Georgi",
                "Georgiev",
                List.of(new SimpleGrantedAuthority("USER"))
        );
    }

    public FitcomUser getAdmin() {
        return new FitcomUser(
                "topsecret",
                "admin@abv.bg",
                "Admin",
                "Admin",
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
    }

    public void initRoles() {
        RoleEntity user = new RoleEntity()
                .setRole(RoleEnum.USER);

        RoleEntity trainer = new RoleEntity()
                .setRole(RoleEnum.TRAINER);

        RoleEntity nutritionist = new RoleEntity()
                .setRole(RoleEnum.NUTRITIONIST);

        RoleEntity admin = new RoleEntity()
                .setRole(RoleEnum.ADMIN);

        roleRepository.saveAll(List.of(user, trainer, nutritionist, admin));
    }

    public void createUser() {
        UserEntity user = new UserEntity()
                .setFirstName("Georgi")
                .setLastName("Georgiev")
                .setEmail("georgi@abv.bg")
                .setPassword(passwordEncoder.encode("test"));

        userRepository.save(user);
    }

    public void createAdmin() {
        UserEntity user = new UserEntity()
                .setFirstName("Admin")
                .setLastName("Admin")
                .setEmail("admin@abv.bg")
                .setPassword(passwordEncoder.encode("test"))
                .setRoles(List.of(roleRepository.findByRole(RoleEnum.ADMIN).get()));

        userRepository.save(user);
    }

    public UserEntity createApplicant() {
        UserEntity userEntity = userRepository.findByEmail("georgi@abv.bg").get();
        userEntity.setPendingRoles(List.of(roleRepository.findByRole(RoleEnum.ADMIN).get()));
        return userRepository.save(userEntity);
    }

    public void createAccount() {
        AccountEntity account = new AccountEntity()
                .setFirstName("Georgi")
                .setLastName("Georgiev")
                .setEmail("georgi@abv.bg")
                .setPassword("test");

        accountRepository.save(account);
    }

    public void createToken(String token) {
        TokenEntity tokenEntity = new TokenEntity()
                .setToken(token)
                .setEmail("georgi@abv.bg")
                .setExpiryDate();

        tokenRepository.save(tokenEntity);
    }

    public void createExercise() {
        ExerciseEntity exercise = new ExerciseEntity()
                .setName("Barbell Bench Press")
                .setDescription("Grasp the bar just outside shoulder-width and arch your back so there’s space between your lower back and the bench.\n" +
                        "Pull the bar out of the rack and lower it to your sternum, tucking your elbows about 45° to your sides.\n" +
                        "When the bar touches your body, drive your feet hard into the floor and press the bar back up.")
                .setVideoUrl("rT7DgCr-3pg");

        exercise.setId(1);

        exerciseRepository.save(exercise);
    }


}
