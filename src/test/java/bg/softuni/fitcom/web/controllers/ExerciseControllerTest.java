package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.entities.ExerciseEntity;
import bg.softuni.fitcom.models.user.FitcomUser;
import bg.softuni.fitcom.repositories.ExerciseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class ExerciseControllerTest {

    private final FitcomUser user = new FitcomUser(
            "topsecret",
            "georgi@abv.bg",
            "Georgi",
            "Georgiev",
            List.of(new SimpleGrantedAuthority("USER"))
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @AfterEach
    void tearDown() {
        exerciseRepository.deleteAll();
    }

    @Test
    void testGetExerciseById_returnsOk() throws Exception {
        createExercise();

        mockMvc.perform(get("/exercises/1").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("exercise-details"));
    }

    @Test
    void testGetExerciseById_returnsNotFound() throws Exception {
        mockMvc.perform(get("/exercises/999").with(user(user)))
                .andExpect(status().isNotFound());
    }

    private void createExercise() {
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
