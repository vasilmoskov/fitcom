package bg.softuni.fitcom.services;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.CommentEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.view.CommentViewModel;
import bg.softuni.fitcom.repositories.CommentRepository;
import bg.softuni.fitcom.services.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    private CommentService serviceToTest;

    @BeforeEach
    void init() {
        serviceToTest = new CommentServiceImpl(commentRepository);
    }

    @Test
    void testGetPending_returnsComments() {
        when(commentRepository.findAllByApproved(false)).thenReturn(createComments());

        List<CommentViewModel> comments = serviceToTest.getPending();
        CommentViewModel comment1 = comments.get(0);
        CommentViewModel comment2 = comments.get(1);

        assertEquals(2, comments.size());
        assertEquals("Wow!", comment1.getTextContent());
        assertEquals("Pesho Petrov", comment1.getAuthor());
        assertEquals("Amazing!", comment2.getTextContent());
        assertEquals("Pesho Petrov", comment2.getAuthor());
    }

    @Test
    void testGetPending_returnsEmptyList() {
        when(commentRepository.findAllByApproved(false)).thenReturn(new ArrayList<>());

        List<CommentViewModel> comments = serviceToTest.getPending();

        assertTrue(comments.isEmpty());
    }

    @Test
    void testApprove_savesComment() {
        CommentEntity comment = createComment();
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        serviceToTest.approve(1L);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testApprove_throwsNotFound() {
        when(commentRepository.findById(2L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> serviceToTest.approve(2L));
    }

    @Test
    void testDelete_deletesComment() {
        CommentEntity comment = createComment();
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        serviceToTest.delete(1L);

        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_throwsNotFound() {
        when(commentRepository.findById(2L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> serviceToTest.delete(2L));
    }

    private CommentEntity createComment() {
        UserEntity userEntity = new UserEntity()
                .setFirstName("Pesho")
                .setLastName("Petrov");

        CommentEntity comment = new CommentEntity()
                .setTextContent("Wow!")
                .setAuthor(userEntity)
                .setApproved(false)
                .setCreated(LocalDateTime.now());

        comment.setId(1L);

        return comment;
    }

    private List<CommentEntity> createComments() {
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
