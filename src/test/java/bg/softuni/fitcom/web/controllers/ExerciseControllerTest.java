package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.utils.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testGetExerciseById_returnsOk() throws Exception {
        testDataUtils.createExercise();

        mockMvc.perform(get("/exercises/1").with(user(testDataUtils.getUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("exercise-details"));
    }

    @Test
    void testGetExerciseById_returnsNotFound() throws Exception {
        mockMvc.perform(get("/exercises/999").with(user(testDataUtils.getUser())))
                .andExpect(status().isNotFound());
    }
}
