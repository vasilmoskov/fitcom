package bg.softuni.fitcom;

import bg.softuni.fitcom.models.entities.BodyPartEntity;
import bg.softuni.fitcom.models.entities.GoalEntity;
import bg.softuni.fitcom.models.entities.ExerciseEntity;
import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.entities.TrainingProgramEntity;
import bg.softuni.fitcom.models.enums.BodyPartEnum;
import bg.softuni.fitcom.models.enums.GoalEnum;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.repositories.BodyPartRepository;
import bg.softuni.fitcom.repositories.ExerciseRepository;
import bg.softuni.fitcom.repositories.GoalRepository;
import bg.softuni.fitcom.repositories.RoleRepository;
import bg.softuni.fitcom.repositories.TrainingProgramRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDateTime;
import java.util.List;

//@Component
public class DbInitializr implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BodyPartRepository bodyPartRepository;
    private final ExerciseRepository exerciseRepository;
    private final GoalRepository goalRepository;
    private final TrainingProgramRepository trainingProgramRepository;

    public DbInitializr(RoleRepository roleRepository, UserRepository userRepository,
                        BodyPartRepository bodyPartRepository, ExerciseRepository exerciseRepository,
                        GoalRepository goalRepository, TrainingProgramRepository trainingProgramRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bodyPartRepository = bodyPartRepository;
        this.exerciseRepository = exerciseRepository;
        this.goalRepository = goalRepository;
        this.trainingProgramRepository = trainingProgramRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedDb();
    }

    private void seedDb() {
        seedRoles();
        seedUsers();
        seedBodyParts();
        seedExercises();
        seedGoals();
        seedTrainingPrograms();
    }

    private void seedRoles() {
        RoleEntity user = new RoleEntity()
                .setRole(RoleEnum.USER);

        RoleEntity trainer = new RoleEntity()
                .setRole(RoleEnum.TRAINER);

        RoleEntity nutritionist = new RoleEntity()
                .setRole(RoleEnum.NUTRITIONIST);

        this.roleRepository.saveAll(List.of(user, trainer, nutritionist));
    }

    private void seedUsers() {
        UserEntity user = new UserEntity()
                .setFirstName("Vasil")
                .setLastName("Moskov")
                .setAge(25)
                .setEmail("moskov.vasil@gmail.com")
                .setPassword("1245")
                .setRoles(List.of(this.roleRepository.getById(1l), this.roleRepository.getById(2l)));

        this.userRepository.save(user);
    }

    private void seedBodyParts() {
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

        this.bodyPartRepository.saveAll(List.of(abs, arms, back, chest, legs, shoulders));
    }

    private void seedExercises() {
        BodyPartEntity chest = this.bodyPartRepository.findByName(BodyPartEnum.CHEST).get();

        ExerciseEntity benchPress = new ExerciseEntity()
                .setName("Barbell Bench Press")
                .setDescription("Grasp the bar just outside shoulder-width and arch your back so there’s space between your lower back and the bench.\n" +
                        "Pull the bar out of the rack and lower it to your sternum, tucking your elbows about 45° to your sides.\n" +
                        "When the bar touches your body, drive your feet hard into the floor and press the bar back up.")
                .setVideoUrl("rT7DgCr-3pg");

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

        this.exerciseRepository.saveAll(List.of(benchPress, inclinePress, crossover));

        BodyPartEntity abs = this.bodyPartRepository.findByName(BodyPartEnum.ABS).get();

        ExerciseEntity dumbbellCrunch = new ExerciseEntity()
                .setName("Dumbbell crunch")
                .setDescription("Lie on your back, holding a dumbbell or weight plate across your chest in both hands. Raise your torso, then lower it, maintaining tension in your uppers abs throughout.")
                .setVideoUrl("7oKWugCTFMY");

        ExerciseEntity plank = new ExerciseEntity()
                .setName("Plank")
                .setDescription("Maintain a strict plank position, with your hips up, your glutes and core braced, and your head and neck relaxed. Breathing slowly and deeply, hold the position for as long as possible.")
                .setVideoUrl("pSHjTRCQxIw");

        this.exerciseRepository.saveAll(List.of(dumbbellCrunch, plank, crossover));
    }

    private void seedGoals() {
        GoalEntity gainMass = new GoalEntity()
                .setName(GoalEnum.GAIN_MASS);

        GoalEntity loseFat = new GoalEntity()
                .setName(GoalEnum.LOSE_FAT);

        this.goalRepository.saveAll(List.of(gainMass, loseFat));
    }

    private void seedTrainingPrograms() {
        TrainingProgramEntity trainingProgramChest = new TrainingProgramEntity()
                .setTitle("Chest Workout")
                .setAuthor(this.userRepository.getById(1L))
                .setExercises(List.of(this.exerciseRepository.getById(1L),
                        this.exerciseRepository.getById(2L), this.exerciseRepository.getById(3L)))
                .setCreated(LocalDateTime.now())
                .setBodyParts(List.of(this.bodyPartRepository.findByName(BodyPartEnum.CHEST).get()))
                .setGoal(this.goalRepository.getById(1L));

        TrainingProgramEntity trainingProgramAbs = new TrainingProgramEntity()
                .setTitle("Abs Workout")
                .setAuthor(this.userRepository.getById(1L))
                .setExercises(List.of(this.exerciseRepository.getById(4L),
                        this.exerciseRepository.getById(5L)))
                .setCreated(LocalDateTime.now())
                .setBodyParts(List.of(this.bodyPartRepository.findByName(BodyPartEnum.CHEST).get()))
                .setGoal(this.goalRepository.getById(2L));

        this.trainingProgramRepository.save(trainingProgramChest);
        this.trainingProgramRepository.save(trainingProgramAbs);
    }
}
