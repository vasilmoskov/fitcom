package bg.softuni.fitcom;

import bg.softuni.fitcom.models.entities.BodyPartEntity;
import bg.softuni.fitcom.models.entities.DietEntity;
import bg.softuni.fitcom.models.entities.GoalEntity;
import bg.softuni.fitcom.models.entities.ExerciseEntity;
import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.entities.TrainingProgramEntity;
import bg.softuni.fitcom.models.enums.BodyPartEnum;
import bg.softuni.fitcom.models.enums.GoalEnum;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.repositories.BodyPartRepository;
import bg.softuni.fitcom.repositories.DietRepository;
import bg.softuni.fitcom.repositories.ExerciseRepository;
import bg.softuni.fitcom.repositories.GoalRepository;
import bg.softuni.fitcom.repositories.RoleRepository;
import bg.softuni.fitcom.repositories.TrainingProgramRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DbInitializr implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BodyPartRepository bodyPartRepository;
    private final GoalRepository goalRepository;
    private final TrainingProgramRepository trainingProgramRepository;
    private final DietRepository dietRepository;

    public DbInitializr(RoleRepository roleRepository, UserRepository userRepository,
                        BodyPartRepository bodyPartRepository, GoalRepository goalRepository,
                        TrainingProgramRepository trainingProgramRepository, DietRepository dietRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bodyPartRepository = bodyPartRepository;
        this.goalRepository = goalRepository;
        this.trainingProgramRepository = trainingProgramRepository;
        this.dietRepository = dietRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedDb();
    }

    private void seedDb() {
        seedRoles();
        seedUsers();
        seedBodyParts();
        seedGoals();
        seedTrainingPrograms();
        seedDiets();
    }

    private void seedRoles() {
        if (roleRepository.count() > 0) {
            return;
        }

        RoleEntity user = new RoleEntity()
                .setRole(RoleEnum.USER);

        RoleEntity admin = new RoleEntity()
                .setRole(RoleEnum.ADMIN);

        RoleEntity trainer = new RoleEntity()
                .setRole(RoleEnum.TRAINER);

        RoleEntity nutritionist = new RoleEntity()
                .setRole(RoleEnum.NUTRITIONIST);

        this.roleRepository.saveAll(List.of(user, admin, trainer, nutritionist));
    }

    private void seedUsers() {
        if (userRepository.count() > 0) {
            return;
        }

        UserEntity user = new UserEntity()
                .setFirstName("Nedelcho")
                .setLastName("Nedelev")
                .setAge(25)
                .setEmail("testdevstuff1@gmail.com")
                .setRoles(List.of(this.roleRepository.findByRole(RoleEnum.ADMIN).get()));

        this.userRepository.save(user);
    }

    private void seedBodyParts() {
        if (bodyPartRepository.count() > 0) {
            return;
        }

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

        this.bodyPartRepository.saveAll(List.of(abs, arms, back, chest, legs, shoulders, other));
    }

    private void seedGoals() {
        if (goalRepository.count() > 0) {
            return;
        }

        GoalEntity gainMass = new GoalEntity()
                .setName(GoalEnum.GAIN_MASS);

        GoalEntity loseFat = new GoalEntity()
                .setName(GoalEnum.LOSE_FAT);

        this.goalRepository.saveAll(List.of(gainMass, loseFat));
    }

    private void seedTrainingPrograms() {
        if (trainingProgramRepository.count() > 0) {
            return;
        }

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

        TrainingProgramEntity trainingProgramChest = new TrainingProgramEntity()
                .setTitle("Chest Workout")
                .setAuthor(this.userRepository.getById(1L))
                .setExercises(List.of(benchPress, inclinePress, crossover))
                .setCreated(LocalDateTime.now())
                .setBodyParts(List.of(this.bodyPartRepository.findByName(BodyPartEnum.CHEST).get()))
                .setGoal(this.goalRepository.getById(1L));

        ExerciseEntity dumbbellCrunch = new ExerciseEntity()
                .setName("Dumbbell crunch")
                .setDescription("Lie on your back, holding a dumbbell or weight plate across your chest in both hands. Raise your torso, then lower it, maintaining tension in your uppers abs throughout.")
                .setVideoUrl("7oKWugCTFMY");

        ExerciseEntity plank = new ExerciseEntity()
                .setName("Plank")
                .setDescription("Maintain a strict plank position, with your hips up, your glutes and core braced, and your head and neck relaxed. Breathing slowly and deeply, hold the position for as long as possible.")
                .setVideoUrl("pSHjTRCQxIw");

        TrainingProgramEntity trainingProgramAbs = new TrainingProgramEntity()
                .setTitle("Abs Workout")
                .setAuthor(this.userRepository.getById(1L))
                .setExercises(List.of(dumbbellCrunch, plank))
                .setCreated(LocalDateTime.now())
                .setBodyParts(List.of(this.bodyPartRepository.findByName(BodyPartEnum.CHEST).get()))
                .setGoal(this.goalRepository.getById(2L));

        ExerciseEntity deadlift = new ExerciseEntity()
                .setName("Deadlift")
                .setDescription("This mighty pull is far more than a back exercise. It hits the entire posterior chain, from your calves to your upper traps, but it's also a time-tested standout for overall backside development.\n")
                .setVideoUrl("ytGaGIn3SjE");

        ExerciseEntity pullUp = new ExerciseEntity()
                .setName("Pull-Up")
                .setDescription("It's always a good idea to have an overhead pulling movement in your back routine, and the pull-up is one of the best. Each variation has its own advantages: Wide-grip variations are great for the upper lats, while close-grip chins or neutral-grip pull-ups have a greater stretch and overall range of motion. Mix it up!")
                .setVideoUrl("eGo4IYlbE5g");

        ExerciseEntity tBarRow = new ExerciseEntity()
                .setName("T-Bar Row")
                .setDescription("The T-bar row may seem at first glance like another variation of the bent-over row, but serious lifters know there's a big difference. For one, you can pile on more weight!")
                .setVideoUrl("OrrKhAcb62o");

        TrainingProgramEntity trainingProgramBack = new TrainingProgramEntity()
                .setTitle("Back Workout")
                .setAuthor(this.userRepository.getById(1L))
                .setExercises(List.of(deadlift, pullUp, tBarRow))
                .setCreated(LocalDateTime.now())
                .setBodyParts(List.of(this.bodyPartRepository.findByName(BodyPartEnum.BACK).get()))
                .setGoal(this.goalRepository.getById(2L));

        ExerciseEntity overheadPress = new ExerciseEntity()
                .setName("Overhead press")
                .setDescription("Stand tall with a barbell across the front of your shoulders. Brace your core, then press the bar directly overhead. Lower it slowly back to the start.")
                .setVideoUrl("_RlRDWO2jfg");

        ExerciseEntity barbellShrug = new ExerciseEntity()
                .setName("Barbell shrug")
                .setDescription("Lower the bar to thigh level then, keeping your arms straight, shrug the bar up so that your shoulders reach your ears. Hold this top position for a second, then lower it back to the start.")
                .setVideoUrl("jTVbilkxSAk");

        TrainingProgramEntity trainingProgramShoulders = new TrainingProgramEntity()
                .setTitle("Shoulders Workout")
                .setAuthor(this.userRepository.getById(1L))
                .setExercises(List.of(overheadPress, barbellShrug))
                .setCreated(LocalDateTime.now())
                .setBodyParts(List.of(this.bodyPartRepository.findByName(BodyPartEnum.SHOULDERS).get()))
                .setGoal(this.goalRepository.getById(2L));

        ExerciseEntity backSquat = new ExerciseEntity()
                .setName("Back squat")
                .setDescription("Load a barbell on your traps and stand with your feet shoulder-width apart. Your gaze should be ahead, your chest should be proud, and your toes should be pointed slightly out.\n" +
                        "Sit back into your hips, bend your knees, and drop down toward the floor. Ensure that your knees move slightly out, and do not collapse in.\n" +
                        "Lower until your thighs are parallel to the ground — or as far down as your mobility allows — then push back up to the starting position.")
                .setVideoUrl("bEv6CCg2BC8");

        TrainingProgramEntity trainingProgramLegs = new TrainingProgramEntity()
                .setTitle("Shoulders Workout")
                .setAuthor(this.userRepository.getById(1L))
                .setExercises(List.of(backSquat))
                .setCreated(LocalDateTime.now())
                .setBodyParts(List.of(this.bodyPartRepository.findByName(BodyPartEnum.LEGS).get()))
                .setGoal(this.goalRepository.getById(2L));

        ExerciseEntity bicepsCurl = new ExerciseEntity()
                .setName("Incline Bicep Curl")
                .setDescription("Sit on an incline bench and hold a dumbbell in each hand at arm's length. Use your biceps to curl the dumbbell until it reaches your shoulder, then lower them back down to your side and repeat.")
                .setVideoUrl("MVSccftvAQw");

        ExerciseEntity concentrationCurl = new ExerciseEntity()
                .setName("Concentration Curl")
                .setDescription("Sit down on bench and rest your right arm against your right leg, letting the weight hang down. Curl the weight up, pause, then lower. Repeat with the other arm.")
                .setVideoUrl("ebqgIOiYGEY");

        ExerciseEntity underhandSeatedRow = new ExerciseEntity()
                .setName("Underhand Seated Row")
                .setDescription("Bend your knees and hold the bar with an underhand grip, shoulder-width apart. Lean back slightly, keeping your back straight, then use your back muscle to drive the bar towards your belly button. Return the bar to starting position and repeat.")
                .setVideoUrl("YUhAXDSL7Ko");

        TrainingProgramEntity trainingProgramArms = new TrainingProgramEntity()
                .setTitle("Shoulders Workout")
                .setAuthor(this.userRepository.getById(1L))
                .setExercises(List.of(bicepsCurl, concentrationCurl, underhandSeatedRow))
                .setCreated(LocalDateTime.now())
                .setBodyParts(List.of(this.bodyPartRepository.findByName(BodyPartEnum.ARMS).get()))
                .setGoal(this.goalRepository.getById(2L));

        this.trainingProgramRepository.saveAll(List.of(trainingProgramChest, trainingProgramAbs, trainingProgramBack,
                trainingProgramShoulders, trainingProgramLegs, trainingProgramArms));
    }

    private void seedDiets() {
        if (dietRepository.count() > 0) {
            return;
        }

        DietEntity mediterraneanDiet = new DietEntity()
                .setTitle("Mediterranean diet")
                .setAuthor(this.userRepository.getById(1L))
                .setDescription("The Mediterranean diet has long been considered the gold standard for nutrition, disease prevention, wellness, and longevity. This is based on its nutrition benefits and sustainability. The Mediterranean diet is based on foods that people in countries like Italy and Greece have traditionally eaten. Foods such as poultry, eggs, and dairy products are to be eaten in moderation, and red meats are limited.")
                .setCreated(LocalDateTime.now())
                .setGoal(this.goalRepository.getById(2L));

        DietEntity dashDiet = new DietEntity()
                .setTitle("DASH diet")
                .setAuthor(this.userRepository.getById(1L))
                .setDescription("Dietary Approaches to Stop Hypertension, or DASH, is an eating plan designed to help treat or prevent high blood pressure, which is clinically known as hypertension.\n" +
                        "\n" +
                        "It emphasizes eating plenty of fruits, vegetables, whole grains, and lean meats. It is low in salt, red meat, added sugars, and fat.\n" +
                        "\n" +
                        "While the DASH diet is not a weight loss diet, many people report losing weight on it.\n" +
                        "\n" +
                        "The DASH diet recommends specific servings of different food groups. The number of servings you are encouraged to eat depends on your daily calorie intake.")
                .setCreated(LocalDateTime.now())
                .setGoal(this.goalRepository.getById(2L));

        DietEntity bulkDiet = new DietEntity()
                .setTitle("The Maximuscle 4 Week Bulking Diet")
                .setAuthor(this.userRepository.getById(1L))
                .setDescription("Your bulking transformation depends on two things: eating at a calculated calorie surplus and eating healthy food. A ‘dirty bulk’ will add too much body fat and leave you feeling bloated. \n" +
                        "\n" +
                        "Instead, aim to consume around 6 meals a day that pack in plenty of calories. We’ve provided plenty of options for each meal below, so mix and match to build yourself a diet that’ll help you quickly gain lean mass. \n" +
                        "\n" +
                        "Add veggies such as tomatoes, celery and broccoli, to whichever meal you like. \n" +
                        "\n" +
                        "Breakfast options – select one from the following list each day. \n" +
                        "\n" +
                        "6 egg omelette with spinach (approx. 564 kcal) \n" +
                        "Large portion of granola, whole milk and sliced banana (approx. 750 kcal) \n" +
                        "1 or 2 bagels with peanut butter (approx. 380/760 kcal) \n" +
                        "2 Poached eggs, salmon and avocado (approx. 550 kcal) \n" +
                        "Morning meal/snack options – combine these to increase calories if needed \n" +
                        "\n" +
                        "Progain flapjack (approx. 324 kcal) \n" +
                        "Progain extreme shake (600 kcal) \n" +
                        "Handful of almonds (approx. 92 kcal per serving) \n" +
                        "Apple with peanut butter (approx. 200 kcal)\n" +
                        "Small portion of chicken breast, tomatoes, celery and brown rice (approx. 450 kcal) \n" +
                        "Lunch Options \n" +
                        "\n" +
                        "Double chicken breast, broccoli and rice (approx. 700 kcal) \n" +
                        "Salmon, sweet potatoes and sesame seeds (approx. 700 kcal) \n" +
                        "Chicken breast, salsa, brown rice and peppers (approx. 720 kcal)\n" +
                        "Vegetarian bean chili burritos (approx. 900 kcal) \n" +
                        "Tinned tuna, quinoa, avocado and broccoli (approx. 500 kcal)\n" +
                        "Snack options (select 1 or 2 per day depending on calories) \n" +
                        "\n" +
                        "Dark chocolate (a single ounce is 153 kcal. Eat as much as your surplus allows) \n" +
                        "Mass gainer shake (approx. 600 kcal) \n" +
                        "Cup of mixed nuts (approx. 640 kcal) \n" +
                        "Beef Jerky (approx. 410 kcal) \n" +
                        "Dinner options \n" +
                        "\n" +
                        "Tuna steak with olive oil, two sweet potatoes and quinoa (approx. 800 kcal) \n" +
                        "Sirloin steak, white rice and fried egg (approx. 950 kcal) \n" +
                        "Burger with lean beef, fries, white bread roll, cup of green beans (approx. 1450 kcal) \n" +
                        "Tuna, pasta and Bolognese sauce (approx. 600 kcal) \n" +
                        "Chili con carne with rolls of bread (approx. 700 kcal)")
                .setCreated(LocalDateTime.now())
                .setGoal(this.goalRepository.getById(1L));

        DietEntity gainMassDiet = new DietEntity()
                .setTitle("The (delicious) mass-gaining diet")
                .setAuthor(this.userRepository.getById(1L))
                .setDescription("Gaining muscle should be fun. But the way some nutritionists write mass-building meal plans, it’s anything but. They have you rigidly counting calories and planning your meals well in advance, making you choose from a short menu of bland foods. Fortunately, none of that is necessary. In fact, it’s not as effective as the plan we offer here.\n" +
                        "\n" +
                        "All you need to gain mass is a steady supply of calories, and the know-how to time your carbs properly. Here’s how it works: eat protein foods, fats, and vegetables up until your workout each day. After training, add carbs to the mix. The amount and type of carbs you eat will vary depending on what kind of training you did that day and when you did it (we have three options for morning, midday, and night trainees). You don’t need to count calories or measure exact portions. On your heaviest workout days and accessory sessions, you’ll be able to eat so-called “bad” carbs liberally— we’re talking all the sugary and starchy foods most experts say to steer clear of—without gaining appreciable fat.\n" +
                        "\n" +
                        "You can do this because resistance training changes the way your body responds to blood sugar spikes. For a few hours after lifting, carbs (especially the fast-digesting, high-glycemic kind) send a huge hormonal growth signal that only the muscle cells respond to. However, the effectiveness is contingent on your keeping carbs as low as possible in your pre-workout meals. If you get a blood sugar rush at any other time of the day, you’re going to shut down your body’s fat-burning process and store calories in your gut.\n" +
                        "\n" +
                        "Keep reading for a list of acceptable foods to eat post-workout, and pay attention to the eating prescriptions for each training day. Your diet on Days 1 and 4 will be different from Days 3 and 6, and so on. On the sample meal plans for morning, midday, and nighttime training, you’ll see directions to consume “protein and carb meals” after your workouts. The guidelines for what to eat and how much pertain to which workout you’re doing, and it’s laid out for you in the respective “Heavy,” “Accessory,“ and “Cardio” categories on the following pages. Where it says to modify your serving of your pre- and post-workout shake, we’re referring to any whey isolate powder (pre) and whey isolate with waxy maize or maltodextrin (or any similar carb powder, for post) that you choose.")
                .setCreated(LocalDateTime.now())
                .setGoal(this.goalRepository.getById(1L));

        dietRepository.saveAll(List.of(mediterraneanDiet, dashDiet, bulkDiet, gainMassDiet));
    }
}
