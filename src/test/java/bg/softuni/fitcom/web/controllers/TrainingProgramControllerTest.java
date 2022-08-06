package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.entities.TrainingProgramEntity;
import bg.softuni.fitcom.models.enums.GoalEnum;
import bg.softuni.fitcom.utils.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainingProgramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testGetPagedTrainingPrograms() throws Exception {
        testDataUtils.createTrainingPrograms();

        mockMvc.perform(get("/training-programs"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trainingPrograms"))
                .andExpect(view().name("training-programs"));
    }

    @Test
    void testGetTrainingProgramDetails_canModifyOwnTraining() throws Exception {
        List<TrainingProgramEntity> trainingPrograms = testDataUtils.createTrainingPrograms();

        mockMvc.perform(get("/training-programs/" + trainingPrograms.get(0).getId())
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trainingProgram"))
                .andExpect(model().attribute("canModify", true))
                .andExpect(model().attribute("isInUserFavourites", false))
                .andExpect(model().attributeExists("comments"))
                .andExpect(view().name("training-program-details"));
    }

    @Test
    void testGetTrainingProgramDetails_canNotModifyOthersTraining() throws Exception {
        List<TrainingProgramEntity> trainingPrograms = testDataUtils.createTrainingPrograms();

        mockMvc.perform(get("/training-programs/" + trainingPrograms.get(1).getId())
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trainingProgram"))
                .andExpect(model().attribute("canModify", false))
                .andExpect(model().attribute("isInUserFavourites", false))
                .andExpect(model().attributeExists("comments"))
                .andExpect(view().name("training-program-details"));
    }

    @Test
    void testGetAdd_returnsForbiddenForNonTrainers() throws Exception {
        mockMvc.perform(get("/training-programs/add")
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/training-programs/add/forbidden"));
    }

    @Test
    void testGetAdd_returnsOkForTrainers() throws Exception {
        mockMvc.perform(get("/training-programs/add")
                        .with(user(testDataUtils.getTrainer())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trainingProgram"))
                .andExpect(model().attributeExists("bodyParts"))
                .andExpect(view().name("training-programs-add"));
    }


    @Test
    void testAddProgram() throws Exception {
        testDataUtils.createUser();
        testDataUtils.createGainMassGoal();
        testDataUtils.createBodyParts();

        mockMvc.perform(post("/training-programs/add")
                        .with(csrf())
                        .with(user(testDataUtils.getTrainer()))
                        .param("title", "Back Workout")
                        .param("description", "When you crack your exercise toolkit open each week on back day, you've got a seemingly endless array of movements available. If you're overwhelmed by the sheer number of row variations, or you draw a total blank when thinking of new exercises to try, consider this list your new back blueprint.")
                        .param("goal", String.valueOf(GoalEnum.GAIN_MASS))
                        .param("exercisesData", "T-Bar Row", "The T-bar row may seem at first glance like another variation of the bent-over row, but serious lifters know there's a big difference. For one, you can pile on more weight!", "OrrKhAcb62o"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testAddProgram_withErrors() throws Exception {
        testDataUtils.createUser();
        testDataUtils.createGainMassGoal();
        testDataUtils.createBodyParts();

        mockMvc.perform(post("/training-programs/add")
                        .with(csrf())
                        .with(user(testDataUtils.getTrainer()))
                        .param("title", "")
                        .param("description", "When you crack your exercise toolkit open each week on back day, you've got a seemingly endless array of movements available. If you're overwhelmed by the sheer number of row variations, or you draw a total blank when thinking of new exercises to try, consider this list your new back blueprint.")
                        .param("goal", String.valueOf(GoalEnum.GAIN_MASS))
                        .param("exercisesData", "T-Bar Row", "The T-bar row may seem at first glance like another variation of the bent-over row, but serious lifters know there's a big difference. For one, you can pile on more weight!", "OrrKhAcb62o"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("trainingProgram"))
                .andExpect(redirectedUrl("add-errors"));
    }

    @Test
    void testGetEdit_canAccessOwnTraining() throws Exception {
        List<TrainingProgramEntity> trainingPrograms = testDataUtils.createTrainingPrograms();

        mockMvc.perform(get("/training-programs/" + trainingPrograms.get(0).getId() + "/edit")
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trainingProgram"))
                .andExpect(model().attributeExists("exercisesData"))
                .andExpect(view().name("training-program-edit"));
    }

    @Test
    void testGetEdit_canNotAccessOthersTraining() throws Exception {
        List<TrainingProgramEntity> trainingPrograms = testDataUtils.createTrainingPrograms();

        mockMvc.perform(get("/training-programs/" + trainingPrograms.get(1).getId() + "/edit")
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    // Not working
    @Test
    void testEditProgram() throws Exception {
        List<TrainingProgramEntity> trainingPrograms = testDataUtils.createTrainingPrograms();

        mockMvc.perform(put("/training-programs/" + trainingPrograms.get(0).getId() + "/edit")
                        .with(csrf())
                        .with(user(testDataUtils.getUser()))
                        .param("title", "Ultimate Back Workout")
                        .param("description", "Your back will surely explode.")
                        .param("goal", String.valueOf(GoalEnum.GAIN_MASS))
                        .param("exercisesData", "Pull Ups", "Make them slow and clean", "eGo4IYlbE5g"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/training-programs"));
    }

    @Test
    void testDeleteTrainingProgram() throws Exception {
        List<TrainingProgramEntity> trainingPrograms = testDataUtils.createTrainingPrograms();

        mockMvc.perform(delete("/training-programs/" + trainingPrograms.get(0).getId())
                        .with(csrf())
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/training-programs"));
    }

    @Test
    void testRemoveExerciseFromTraining() throws Exception {
        List<TrainingProgramEntity> trainingPrograms = testDataUtils.createTrainingPrograms();

        mockMvc.perform(delete("/training-programs/" + trainingPrograms.get(0).getId() + "/remove-exercise")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("Cable Crossover"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAddAndRemoveFromFavourites() throws Exception {
        List<TrainingProgramEntity> trainingPrograms = testDataUtils.createTrainingPrograms();

        mockMvc.perform(post("/training-programs/" + trainingPrograms.get(1).getId() + "/add-to-favourites")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/training-programs/" + trainingPrograms.get(1).getId()));

        mockMvc.perform(post("/training-programs/" + trainingPrograms.get(1).getId() + "/remove-from-favourites")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/training-programs/" + trainingPrograms.get(1).getId()));
    }

    @Test
    void testAddComment() throws Exception {
        List<TrainingProgramEntity> trainingPrograms = testDataUtils.createTrainingPrograms();

        mockMvc.perform(post("/training-programs/" + trainingPrograms.get(1).getId() + "/comment")
                        .param("textContent", "Amazing training!")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/training-programs/" + trainingPrograms.get(1).getId()));
    }

    @Test
    void testAddComment_invalidComment() throws Exception {
        List<TrainingProgramEntity> trainingPrograms = testDataUtils.createTrainingPrograms();

        mockMvc.perform(post("/training-programs/" + trainingPrograms.get(1).getId() + "/comment")
                        .param("textContent", "")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("commentBindingModel"))
                .andExpect(redirectedUrl("/training-programs/" + trainingPrograms.get(1).getId()));
    }
}
