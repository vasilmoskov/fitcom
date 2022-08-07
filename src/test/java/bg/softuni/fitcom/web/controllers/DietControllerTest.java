package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.DbInitializr;
import bg.softuni.fitcom.models.entities.DietEntity;
import bg.softuni.fitcom.models.enums.GoalEnum;
import bg.softuni.fitcom.utils.TestControllerUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class DietControllerTest {
    @MockBean
    private DbInitializr dbInitializr;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestControllerUtils testDataUtils;

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testGetDiets() throws Exception {
        testDataUtils.createDiets();

        mockMvc.perform(get("/diets"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("diets"))
                .andExpect(view().name("diets"));
    }

    @Test
    void testGetDietDetails_canModifyOwnDiet() throws Exception {
        List<DietEntity> diets = testDataUtils.createDiets();

        mockMvc.perform(get("/diets/" + diets.get(0).getId())
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("diet"))
                .andExpect(model().attribute("canModify", true))
                .andExpect(model().attribute("isInUserFavourites", false))
                .andExpect(model().attributeExists("comments"))
                .andExpect(view().name("diet-details"));
    }


    @Test
    void testGetDietDetails_canNotModifyOthersDiet() throws Exception {
        List<DietEntity> diets = testDataUtils.createDiets();

        mockMvc.perform(get("/diets/" + diets.get(1).getId())
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("diet"))
                .andExpect(model().attribute("canModify", false))
                .andExpect(model().attribute("isInUserFavourites", false))
                .andExpect(model().attributeExists("comments"))
                .andExpect(view().name("diet-details"));
    }

    @Test
    void testGetAdd_returnsForbiddenForNonNutritionists() throws Exception {
        mockMvc.perform(get("/diets/add")
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/diets/add/forbidden"));
    }

    @Test
    void testGetAdd_returnsOkForNutritionists() throws Exception {
        mockMvc.perform(get("/diets/add")
                        .with(user(testDataUtils.getNutritionist())))
                .andExpect(status().isOk())
                .andExpect(view().name("diets-add"));
    }

    @Test
    void testAddDiet() throws Exception {
        testDataUtils.createUser();
        testDataUtils.createGainMassGoal();

        mockMvc.perform(post("/diets/add")
                        .with(csrf())
                        .with(user(testDataUtils.getNutritionist()))
                        .param("title", "The Maximuscle 4 Week Bulking Diet")
                        .param("description", "You will gain many kilos.")
                        .param("goal", String.valueOf(GoalEnum.GAIN_MASS)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testAddDiet_withErrors() throws Exception {
        testDataUtils.createUser();
        testDataUtils.createGainMassGoal();

        mockMvc.perform(post("/diets/add")
                        .with(csrf())
                        .with(user(testDataUtils.getNutritionist()))
                        .param("title", "")
                        .param("description", "You will gain many kilos.")
                        .param("goal", String.valueOf(GoalEnum.GAIN_MASS)))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("dietBindingModel"))
                .andExpect(redirectedUrl("/diets/add"));
    }

    @Test
    void testGetEdit_canAccessOwnDiet() throws Exception {
        List<DietEntity> diets = testDataUtils.createDiets();

        mockMvc.perform(get("/diets/" + diets.get(0).getId() + "/edit")
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("diet"))
                .andExpect(view().name("diet-edit"));
    }

    @Test
    void testGetEdit_canNotAccessOthersTraining() throws Exception {
        List<DietEntity> diets = testDataUtils.createDiets();

        mockMvc.perform(get("/diets/" + diets.get(1).getId() + "/edit")
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @Test
    void testEditDiet() throws Exception {
        List<DietEntity> diets = testDataUtils.createDiets();

        mockMvc.perform(put("/diets/" + diets.get(0).getId() + "/edit")
                        .with(csrf())
                        .with(user(testDataUtils.getUser()))
                        .param("title", "Mega Bulk diet")
                        .param("description", "You will gain 10 kilos.")
                        .param("goal", String.valueOf(GoalEnum.GAIN_MASS)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/diets/" + diets.get(0).getId()));
    }

    @Test
    void testEditDiet_withErrors() throws Exception {
        List<DietEntity> diets = testDataUtils.createDiets();

        mockMvc.perform(put("/diets/" + diets.get(0).getId() + "/edit")
                        .with(csrf())
                        .with(user(testDataUtils.getUser()))
                        .param("title", "")
                        .param("description", "You will gain 10 kilos.")
                        .param("goal", String.valueOf(GoalEnum.GAIN_MASS)))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("diet"))
                .andExpect(redirectedUrl("edit-errors"));
    }

    @Test
    void testDeleteDiet() throws Exception {
        List<DietEntity> diets = testDataUtils.createDiets();

        mockMvc.perform(delete("/diets/" + diets.get(0).getId())
                        .with(csrf())
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/diets"));
    }

    @Test
    void testAddAndRemoveFromFavourites() throws Exception {
        List<DietEntity> diets = testDataUtils.createDiets();

        mockMvc.perform(post("/diets/" + diets.get(1).getId() + "/add-to-favourites")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/diets/" + diets.get(1).getId()));

        mockMvc.perform(post("/diets/" + diets.get(1).getId() + "/remove-from-favourites")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/diets/" + diets.get(1).getId()));
    }

    @Test
    void testAddComment() throws Exception {
        List<DietEntity> diets = testDataUtils.createDiets();

        mockMvc.perform(post("/diets/" + diets.get(1).getId() + "/comment")
                        .param("textContent", "Amazing diet!")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/diets/" + diets.get(1).getId()));
    }

    @Test
    void testAddComment_invalidComment() throws Exception {
        List<DietEntity> diets = testDataUtils.createDiets();

        mockMvc.perform(post("/diets/" + diets.get(1).getId() + "/comment")
                        .param("textContent", "")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("commentBindingModel"))
                .andExpect(redirectedUrl("/diets/" + diets.get(1).getId()));
    }
}
