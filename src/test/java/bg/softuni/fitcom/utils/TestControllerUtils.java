package bg.softuni.fitcom.utils;

import bg.softuni.fitcom.models.entities.AccountEntity;
import bg.softuni.fitcom.models.entities.BodyPartEntity;
import bg.softuni.fitcom.models.entities.CommentEntity;
import bg.softuni.fitcom.models.entities.ExerciseEntity;
import bg.softuni.fitcom.models.entities.GoalEntity;
import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.TokenEntity;
import bg.softuni.fitcom.models.entities.TrainingProgramEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.enums.BodyPartEnum;
import bg.softuni.fitcom.models.enums.GoalEnum;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.user.FitcomUser;
import bg.softuni.fitcom.repositories.AccountRepository;
import bg.softuni.fitcom.repositories.BodyPartRepository;
import bg.softuni.fitcom.repositories.CommentRepository;
import bg.softuni.fitcom.repositories.ExerciseRepository;
import bg.softuni.fitcom.repositories.GoalRepository;
import bg.softuni.fitcom.repositories.RoleRepository;
import bg.softuni.fitcom.repositories.TokenRepository;
import bg.softuni.fitcom.repositories.TrainingProgramRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestControllerUtils {

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

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TrainingProgramRepository trainingProgramRepository;

    @Autowired
    private BodyPartRepository bodyPartRepository;

    @Autowired
    private GoalRepository goalRepository;

    public void cleanUpDatabase() {
        tokenRepository.deleteAll();
        accountRepository.deleteAll();
        commentRepository.deleteAll();
        trainingProgramRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        exerciseRepository.deleteAll();
        goalRepository.deleteAll();
        bodyPartRepository.deleteAll();
    }

    public FitcomUser getUser() {
        return new FitcomUser(
                "test",
                "georgi@abv.bg",
                "Georgi",
                "Georgiev",
                List.of(new SimpleGrantedAuthority("USER"))
        );
    }

    public FitcomUser getTrainer() {
        return new FitcomUser(
                "test",
                "georgi@abv.bg",
                "Georgi",
                "Georgiev",
                List.of(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("TRAINER"))
        );
    }

    public FitcomUser getAdmin() {
        return new FitcomUser(
                "test",
                "admin@abv.bg",
                "Admin",
                "Admin",
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
    }

    public void initRoles() {
        if (roleRepository.count() > 0) {
            return;
        }

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

    public UserEntity createUser() {
        UserEntity user = new UserEntity()
                .setFirstName("Georgi")
                .setLastName("Georgiev")
                .setEmail("georgi@abv.bg")
                .setPassword(passwordEncoder.encode("test"));

        return userRepository.save(user);
    }

    public UserEntity createAdmin() {
        if (roleRepository.count() == 0) {
            initRoles();
        }

        UserEntity user = new UserEntity()
                .setFirstName("Admin")
                .setLastName("Admin")
                .setEmail("admin@abv.bg")
                .setPassword(passwordEncoder.encode("test"))
                .setRoles(List.of(roleRepository.findByRole(RoleEnum.ADMIN).get()));

        return userRepository.save(user);
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

    public List<ExerciseEntity> createChestExercises() {
        ExerciseEntity inclinePress = new ExerciseEntity()
                .setName("Smith Machine Incline Press")
                .setDescription("Set an adjustable bench to a 30°-45° incline, and roll it into the center of a Smith machine rack.\n" +
                        "Grasp the bar with an overhand, shoulder-width grip.\n" +
                        "Unrack the bar, lower it to the upper part of your chest, and press straight up.")
                .setVideoUrl("EeLLZMdg6zI");

        ExerciseEntity crossover = new ExerciseEntity()
                .setName("Cable Crossover")
                .setDescription("Stand between two facing cable stations with both pulleys set midway between the top and bottom of the station.\n" +
                        "Attach a D-handle to each pulley and hold one in each hand.\n" +
                        "Keep your elbows slightly bent, and step forward so there’s tension on the cables.\n" +
                        "Flex your pecs as you bring your hands together out in front of your chest. Alternate stretching and flexing after each set.")
                .setVideoUrl("taI4XduLpTk");

        return List.of(inclinePress, crossover);
    }

    public List<ExerciseEntity> createAbsExercises() {
        ExerciseEntity dumbbellCrunch = new ExerciseEntity()
                .setName("Dumbbell crunch")
                .setDescription("Lie on your back, holding a dumbbell or weight plate across your chest in both hands. Raise your torso, then lower it, maintaining tension in your uppers abs throughout.")
                .setVideoUrl("7oKWugCTFMY");

        ExerciseEntity plank = new ExerciseEntity()
                .setName("Plank")
                .setDescription("Maintain a strict plank position, with your hips up, your glutes and core braced, and your head and neck relaxed. Breathing slowly and deeply, hold the position for as long as possible.")
                .setVideoUrl("pSHjTRCQxIw");

        return List.of(dumbbellCrunch, plank);
    }

    public CommentEntity createComment() {
        UserEntity user = createUser();

        CommentEntity comment = new CommentEntity()
                .setTextContent("Wow!")
                .setAuthor(user)
                .setApproved(false)
                .setCreated(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public List<BodyPartEntity> createBodyParts() {
        BodyPartEntity abs = new BodyPartEntity()
                .setName(BodyPartEnum.ABS);

        BodyPartEntity arms = new BodyPartEntity()
                .setName(BodyPartEnum.ARMS);

        BodyPartEntity back = new BodyPartEntity()
                .setName(BodyPartEnum.BACK);

        BodyPartEntity chest = new BodyPartEntity()
                .setName(BodyPartEnum.CHEST);

        BodyPartEntity legs = new BodyPartEntity()
                .setName(BodyPartEnum.LEGS);

        BodyPartEntity shoulders = new BodyPartEntity()
                .setName(BodyPartEnum.SHOULDERS);

        BodyPartEntity other = new BodyPartEntity()
                .setName(BodyPartEnum.OTHER);

        return this.bodyPartRepository.saveAll(List.of(abs, arms, back, chest, legs, shoulders, other));
    }

    public GoalEntity createGainMassGoal() {
        GoalEntity gainMass = new GoalEntity()
                .setName(GoalEnum.GAIN_MASS);

        return this.goalRepository.save(gainMass);
    }

    public GoalEntity createLoseFatGoal() {
        GoalEntity loseFat = new GoalEntity()
                .setName(GoalEnum.LOSE_FAT);

        return this.goalRepository.save(loseFat);
    }

    public List<TrainingProgramEntity> createTrainingPrograms() {
        GoalEntity loseFatGoal = createLoseFatGoal();
        GoalEntity gainMassGoal = createGainMassGoal();
        List<ExerciseEntity> chestExercises = createChestExercises();
        List<ExerciseEntity> absExercises = createAbsExercises();
        UserEntity user = createUser();
        UserEntity admin = createAdmin();

        List<BodyPartEntity> bodyParts = bodyPartRepository.count() == 0
                ? createBodyParts()
                : new ArrayList<>();

        List<BodyPartEntity> chest = bodyParts
                .stream()
                .filter(b -> b.getName().equals(BodyPartEnum.CHEST))
                .toList();

        List<BodyPartEntity> abs = bodyParts
                .stream()
                .filter(b -> b.getName().equals(BodyPartEnum.ABS))
                .toList();

        TrainingProgramEntity trainingProgramChest = new TrainingProgramEntity()
                .setTitle("Chest Workout")
                .setAuthor(user)
                .setExercises(chestExercises)
                .setCreated(LocalDateTime.now())
                .setBodyParts(chest)
                .setGoal(gainMassGoal);

        TrainingProgramEntity trainingProgramAbs = new TrainingProgramEntity()
                .setTitle("Abs Workout")
                .setAuthor(admin)
                .setExercises(absExercises)
                .setCreated(LocalDateTime.now())
                .setBodyParts(abs)
                .setGoal(loseFatGoal);

        return trainingProgramRepository.saveAll(List.of(trainingProgramChest, trainingProgramAbs ));
    }
}
