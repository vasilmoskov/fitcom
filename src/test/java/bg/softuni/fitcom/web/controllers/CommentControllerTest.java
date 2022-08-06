package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.entities.CommentEntity;
import bg.softuni.fitcom.utils.TestControllerUtils;
import org.junit.jupiter.api.AfterEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private TestControllerUtils testDataUtils;

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testGetPendingComments() throws Exception {
        testDataUtils.createAdmin();

        mockMvc.perform(get("/comments/pending")
                        .with(user(testDataUtils.getAdmin())))
                .andExpect(status().isOk())
                .andExpect(view().name("pending"))
                .andExpect(model().attributeExists("comments"));
    }

    @Test
    void testGetPendingComments_forbiddenForUsers() throws Exception {
        testDataUtils.createUser();

        mockMvc.perform(get("/comments/pending")
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @Test
    void testApprove() throws Exception {
        testDataUtils.createAdmin();
        CommentEntity comment = testDataUtils.createComment();

        mockMvc.perform(post("/comments/pending/" + comment.getId())
                        .with(csrf())
                        .with(user(testDataUtils.getAdmin())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments/pending"));
    }

    @Test
    void testApprove_returnsForbiddenForUser() throws Exception {
        CommentEntity comment = testDataUtils.createComment();

        mockMvc.perform(post("/comments/pending/" + comment.getId())
                        .with(csrf())
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @Test
    void testDelete() throws Exception {
        testDataUtils.createAdmin();
        CommentEntity comment = testDataUtils.createComment();

        mockMvc.perform(delete("/comments/pending/" + comment.getId())
                        .with(csrf())
                        .with(user(testDataUtils.getAdmin())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments/pending"));
    }

    @Test
    void testDelete_returnsForbiddenWhenAccessedByUser() throws Exception {
        CommentEntity comment = testDataUtils.createComment();

        mockMvc.perform(delete("/comments/pending/" + comment.getId())
                        .with(csrf())
                        .with(user(testDataUtils.getUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }
}
