package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.utils.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    @BeforeEach
    void init() {
        testDataUtils.initRoles();
        testDataUtils.createUser();
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testGetProfile() throws Exception {
        mockMvc.perform(get("/profile").with(user(testDataUtils.getUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    void testGetEditProfile() throws Exception {
        mockMvc.perform(get("/profile/edit").with(user(testDataUtils.getUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("profile-edit"));
    }

    @Test
    void testEditProfile() throws Exception {
        mockMvc.perform(put("/profile/edit")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf())
                        .param("firstName", "Gosho")
                        .param("lastName", "Geshev")
                        .param("email", "gosho@abv.bg")
                        .param("age", "44"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    void testEditProfileWithInvalidData() throws Exception {
        mockMvc.perform(put("/profile/edit")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf())
                        .param("firstName", "G8")
                        .param("lastName", "G7")
                        .param("email", "goshoabv.bg")
                        .param("age", "-44"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("edit-errors"))
                .andExpect(flash().attributeExists("userModel"));
    }

    @Test
    void testGetFavourites() throws Exception {
        mockMvc.perform(get("/favourites").with(user(testDataUtils.getUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("favourites"))
                .andExpect(model().attributeExists("diets", "trainingPrograms"));
    }

    @Test
    void testGetPosts() throws Exception {
        mockMvc.perform(get("/posts").with(user(testDataUtils.getUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("diets", "trainingPrograms"));
    }

    @Test
    void testApplyForNutritionist() throws Exception {
        mockMvc.perform(post("/apply/nutritionist")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testApplyForTrainer() throws Exception {
        mockMvc.perform(post("/apply/trainer")
                        .with(user(testDataUtils.getUser()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testGetPendingApplications_returnsForbiddenWhenUserIsNotAdmin() throws Exception {
        mockMvc.perform(get("/pending/applications")
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @Test
    void testGetPendingApplications() throws Exception {
        testDataUtils.createAdmin();

        mockMvc.perform(get("/pending/applications")
                        .with(user(testDataUtils.getAdmin())))
                .andExpect(status().isOk())
                .andExpect(view().name("applications"))
                .andExpect(model().attributeExists("applications"));
    }

    @Test
    void testApprove() throws Exception {
        testDataUtils.createAdmin();
        UserEntity applicant = testDataUtils.createApplicant();

        mockMvc.perform(post("/pending/applications/" + applicant.getId())
                        .with(csrf())
                        .with(user(testDataUtils.getAdmin())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pending/applications"));
    }

    @Test
    void testApprove_returnsForbiddenWhenAccessedByUser() throws Exception {
        UserEntity applicant = testDataUtils.createApplicant();

        mockMvc.perform(post("/pending/applications/" + applicant.getId())
                        .with(csrf())
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @Test
    void testDelete() throws Exception {
        testDataUtils.createAdmin();
        UserEntity applicant = testDataUtils.createApplicant();

        mockMvc.perform(delete("/pending/applications/" + applicant.getId())
                        .with(csrf())
                        .with(user(testDataUtils.getAdmin())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pending/applications"));
    }

    @Test
    void testDelete_returnsForbiddenWhenAccessedByUser() throws Exception {
        UserEntity applicant = testDataUtils.createApplicant();

        mockMvc.perform(delete("/pending/applications/" + applicant.getId())
                        .with(csrf())
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @Test
    void testGetStats() throws Exception {
        testDataUtils.createAdmin();

        mockMvc.perform(get("/stats")
                .with(user(testDataUtils.getAdmin())))
                .andExpect(status().isOk())
                .andExpect(view().name("stats"))
                .andExpect(model().attributeExists("trainingProgramsToViews", "dietsToViews"));
    }

    @Test
    void testGetStats_returnsForbiddenWhenAccessedByUser() throws Exception {
        mockMvc.perform(get("/stats")
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }
}
