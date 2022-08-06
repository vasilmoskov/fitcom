package bg.softuni.fitcom.utils;

import bg.softuni.fitcom.models.entities.BodyPartEntity;
import bg.softuni.fitcom.models.entities.CommentEntity;
import bg.softuni.fitcom.models.entities.ExerciseEntity;
import bg.softuni.fitcom.models.entities.GoalEntity;
import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.TrainingProgramEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.enums.BodyPartEnum;
import bg.softuni.fitcom.models.enums.GoalEnum;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.service.CommentAddServiceModel;
import bg.softuni.fitcom.models.service.TrainingProgramServiceModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestServiceUtils {
    public TrainingProgramEntity createChestWorkout() {
        TrainingProgramEntity trainingProgramChest = new TrainingProgramEntity()
                .setTitle("Chest Workout")
                .setAuthor(createUser())
                .setExercises(createChestExercises())
                .setBodyParts(List.of(new BodyPartEntity().setName(BodyPartEnum.CHEST)))
                .setCreated(LocalDateTime.now())
                .setGoal(createGainMassGoal());

        trainingProgramChest.setId(1);

        return trainingProgramChest;
    }

    public TrainingProgramEntity createAbsWorkout() {
        TrainingProgramEntity trainingProgramAbs = new TrainingProgramEntity()
                .setTitle("Abs Workout")
                .setAuthor(createAnotherUser())
                .setExercises(createAbsExercises())
                .setBodyParts(List.of(new BodyPartEntity().setName(BodyPartEnum.ABS)))
                .setCreated(LocalDateTime.now())
                .setGoal(createLoseFatGoal());

        trainingProgramAbs.setId(2);

        return trainingProgramAbs;
    }

    public TrainingProgramServiceModel createTrainingProgramServiceModel() {
        return new TrainingProgramServiceModel()
                .setTitle("Abs Workout")
                .setAuthor("pesho@abv.bg")
                .setExercises(new ArrayList<>())
                .setBodyParts(List.of(BodyPartEnum.ABS))
                .setGoal(GoalEnum.LOSE_FAT);
    }

    public List<TrainingProgramEntity> createTrainingPrograms() {
        return List.of(createChestWorkout(), createAbsWorkout());
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
        List<ExerciseEntity> exerciseEntities = new  ArrayList<>();
        ExerciseEntity dumbbellCrunch = new ExerciseEntity()
                .setName("Dumbbell crunch")
                .setDescription("Lie on your back, holding a dumbbell or weight plate across your chest in both hands. Raise your torso, then lower it, maintaining tension in your uppers abs throughout.")
                .setVideoUrl("7oKWugCTFMY");

        ExerciseEntity plank = new ExerciseEntity()
                .setName("Plank")
                .setDescription("Maintain a strict plank position, with your hips up, your glutes and core braced, and your head and neck relaxed. Breathing slowly and deeply, hold the position for as long as possible.")
                .setVideoUrl("pSHjTRCQxIw");

        exerciseEntities.add(dumbbellCrunch);
        exerciseEntities.add(plank);

        return exerciseEntities;
    }

    public CommentEntity createComment() {
        return new CommentEntity()
                .setTextContent("Wow!")
                .setAuthor(createUser())
                .setApproved(false)
                .setCreated(LocalDateTime.now());
    }

    public UserEntity createUser() {
        UserEntity userEntity = new UserEntity()
                .setFirstName("Georgi")
                .setLastName("Georgiev")
                .setEmail("georgi@abv.bg")
                .setPassword("test")
                .setRoles(List.of(new RoleEntity().setRole(RoleEnum.TRAINER)));

        userEntity.setId(1);

        return userEntity;
    }

    public UserEntity createAnotherUser() {
        UserEntity userEntity = new UserEntity()
                .setFirstName("Pesho")
                .setLastName("Petrov")
                .setEmail("pesho@abv.bg")
                .setPassword("test")
                .setRoles(List.of(new RoleEntity().setRole(RoleEnum.NUTRITIONIST)));

        userEntity.setId(2);

        return userEntity;
    }

    public UserEntity createAdmin() {
        UserEntity userEntity = new UserEntity()
                .setFirstName("Admin")
                .setLastName("Admin")
                .setEmail("admin@abv.bg")
                .setPassword("test")
                .setRoles(List.of(new RoleEntity().setRole(RoleEnum.ADMIN)));

        userEntity.setId(3);

        return userEntity;
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

        return List.of(abs, arms, back, chest, legs, shoulders, other);
    }

    public GoalEntity createGainMassGoal() {
        return new GoalEntity()
                .setName(GoalEnum.GAIN_MASS);
    }

    public GoalEntity createLoseFatGoal() {
        return new GoalEntity()
                .setName(GoalEnum.LOSE_FAT);
    }

    public CommentAddServiceModel createCommentServiceModel() {
        return new CommentAddServiceModel()
                .setTextContent("Amazing!")
                .setAuthor("georgi@abv.bg")
                .setCreated(LocalDateTime.now())
                .setApproved(false);
    }

    public List<CommentEntity> createComments() {
        UserEntity userEntity = new UserEntity()
                .setFirstName("Pesho")
                .setLastName("Petrov");

        CommentEntity comment1 = new CommentEntity()
                .setTextContent("Wow!")
                .setAuthor(userEntity)
                .setApproved(false)
                .setCreated(LocalDateTime.now());

        CommentEntity comment2 = new CommentEntity()
                .setTextContent("Amazing!")
                .setAuthor(userEntity)
                .setApproved(false)
                .setCreated(LocalDateTime.now());

        return List.of(comment1, comment2);
    }
}
