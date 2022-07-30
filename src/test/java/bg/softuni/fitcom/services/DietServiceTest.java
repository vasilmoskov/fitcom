package bg.softuni.fitcom.services;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.CommentEntity;
import bg.softuni.fitcom.models.entities.DietEntity;
import bg.softuni.fitcom.models.entities.GoalEntity;
import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.enums.GoalEnum;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.service.CommentAddServiceModel;
import bg.softuni.fitcom.models.service.DietServiceModel;
import bg.softuni.fitcom.models.view.CommentViewModel;
import bg.softuni.fitcom.models.view.DietDetailsViewModel;
import bg.softuni.fitcom.models.view.DietOverviewViewModel;
import bg.softuni.fitcom.repositories.CommentRepository;
import bg.softuni.fitcom.repositories.DietRepository;
import bg.softuni.fitcom.repositories.GoalRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import bg.softuni.fitcom.services.impl.DietServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DietServiceTest {

    @Mock
    private DietRepository dietRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ModelMapper modelMapper;

    private DietService serviceToTest;

    @BeforeEach
    void setup() {
        serviceToTest = new DietServiceImpl(dietRepository, userRepository, goalRepository,
                commentRepository, modelMapper);
    }

    @Test
    void testGetDiets() {
        Pageable pageable = PageRequest.of(0, 2);

        when(dietRepository.findAll(pageable)).thenReturn(createPageOfDiets());

        Page<DietOverviewViewModel> pageOfDiets = serviceToTest.getDiets(pageable);
        List<DietOverviewViewModel> diets = pageOfDiets.getContent();
        DietOverviewViewModel diet1 = diets.get(0);
        DietOverviewViewModel diet2 = diets.get(1);

        assertEquals(1, pageOfDiets.getTotalPages());
        assertEquals(2, diets.size());
        assertEquals("Diet 1", diet1.getTitle());
        assertEquals("Very healthy!", diet1.getDescription());
        assertEquals("Pesho Petrov", diet1.getAuthor());
        assertEquals("Diet 2", diet2.getTitle());
        assertEquals("With this diet you can easily gain kilos and you w...", diet2.getDescription());
        assertEquals("Pesho Petrov", diet2.getAuthor());
    }

    @Test
    void testUpdateDiet() {
        DietEntity diet = createDiet();

        when(userRepository.findByEmail("pesho@abv.bg")).thenReturn(Optional.ofNullable(createUser()));
        when(goalRepository.findByName(GoalEnum.LOSE_FAT)).thenReturn(Optional.ofNullable(createLoseFatGoal()));
        when(dietRepository.findById(1L)).thenReturn(Optional.of(diet));

        serviceToTest.updateDiet(createDietServiceModel());

        verify(dietRepository, times(1)).save(diet);
    }

    @Test
    void testGetDiet() {
        when(dietRepository.findById(1L)).thenReturn(Optional.of(createDiet()));

        DietDetailsViewModel diet = serviceToTest.getDiet(1L);

        assertEquals(1, diet.getId());
        assertEquals("Diet 1", diet.getTitle());
        assertEquals("Very healthy!", diet.getDescription());
        assertEquals("Pesho Petrov", diet.getAuthor());
    }

    @Test
    void testGetDiet_throwsNotFound() {
        when(dietRepository.findById(3L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> serviceToTest.getDiet(3));
    }

    @Test
    void testCanModify_returnsTrueWhenUserIsOwner() {
        when(userRepository.findByEmail("pesho@abv.bg")).thenReturn(Optional.of(createUser()));
        when(dietRepository.findById(1L)).thenReturn(Optional.of(createDiet()));

        boolean canModify = serviceToTest.canModify("pesho@abv.bg", 1);

        assertTrue(canModify);
    }

    @Test
    void testCanModify_returnsFalseWhenUserIsNotOwner() {
        when(userRepository.findByEmail("misho@abv.bg")).thenReturn(Optional.of(createAnotherUser()));
        when(dietRepository.findById(1L)).thenReturn(Optional.of(createDiet()));

        boolean canModify = serviceToTest.canModify("misho@abv.bg", 1);

        assertFalse(canModify);
    }

    @Test
    void testIsInUserFavourites_returnsTrue() {
        DietEntity diet = createDiet();
        UserEntity user = createAnotherUser()
                .addFavouriteDiet(diet);

        when(userRepository.findByEmail("misho@abv.bg")).thenReturn(Optional.of(user));
        when(dietRepository.findById(1L)).thenReturn(Optional.of(diet));

        boolean inUserFavourites = serviceToTest.isInUserFavourites("misho@abv.bg", 1);

        assertTrue(inUserFavourites);
    }

    @Test
    void testIsInUserFavourites_returnsFalse() {
        when(userRepository.findByEmail("misho@abv.bg")).thenReturn(Optional.of(createAnotherUser()));
        when(dietRepository.findById(2L)).thenReturn(Optional.of(createAnotherDiet()));

        boolean inUserFavourites = serviceToTest.isInUserFavourites("misho@abv.bg", 2);

        assertFalse(inUserFavourites);
    }

    @Test
    void testDeleteDiet() {
        serviceToTest.deleteDiet(1L);

        verify(dietRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAddToFavourites() {
        UserEntity user = createAnotherUser();
        DietEntity diet = createDiet();

        when(userRepository.findByEmail("misho@abv.bg")).thenReturn(Optional.of(user));
        when(dietRepository.findById(1L)).thenReturn(Optional.of(diet));

        serviceToTest.addToFavourites(1, "misho@abv.bg");
        List<DietEntity> favouriteDiets = user.getFavouriteDiets();

        verify(userRepository, times(1)).save(user);
        assertEquals(1, favouriteDiets.size());
        assertSame(diet, favouriteDiets.get(0));
    }

    @Test
    void testRemoveFromFavourites() {
        DietEntity diet = createDiet();
        UserEntity user = createAnotherUser()
                .addFavouriteDiet(diet);

        when(userRepository.findByEmail("misho@abv.bg")).thenReturn(Optional.of(user));
        when(dietRepository.findById(1L)).thenReturn(Optional.of(diet));

        serviceToTest.removeFromFavourites(1, "misho@abv.bg");
        List<DietEntity> favouriteDiets = user.getFavouriteDiets();

        verify(userRepository, times(1)).save(user);
        assertTrue(favouriteDiets.isEmpty());
    }

    @Test
    void testAddComment() {
        UserEntity user = createUser();
        DietEntity diet = createDiet();
        CommentAddServiceModel serviceModel = createCommentServiceModel();

        CommentEntity comment = new CommentEntity()
                .setTextContent(serviceModel.getTextContent())
                .setApproved(serviceModel.getApproved())
                .setCreated(serviceModel.getCreated())
                .setAuthor(user)
                .setDiet(diet);

        when(userRepository.findByEmail("pesho@abv.bg")).thenReturn(Optional.of(user));
        when(dietRepository.findById(1L)).thenReturn(Optional.of(diet));
        when(modelMapper.map(serviceModel, CommentEntity.class)).thenReturn(comment);

        serviceToTest.addComment(serviceModel, 1);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testGetComments() {
        DietEntity diet = createDiet();

        when(dietRepository.findById(1L)).thenReturn(Optional.of(diet));
        when(commentRepository.findAllByDietAndApproved(diet, true)).thenReturn(List.of(createComment()));

        List<CommentViewModel> comments = serviceToTest.getComments(1);
        CommentViewModel comment = comments.get(0);

        assertEquals(1, comments.size());
        assertEquals("Thats hard!" , comment.getTextContent());
        assertEquals("Pesho Petrov" , comment.getAuthor());
    }

    private CommentEntity createComment() {
        UserEntity user = createUser();
        DietEntity diet = createDiet();

        return new CommentEntity()
                .setTextContent("Thats hard!")
                .setApproved(true)
                .setCreated(LocalDateTime.now())
                .setAuthor(user)
                .setDiet(diet);
    }

    private CommentAddServiceModel createCommentServiceModel() {
        return new CommentAddServiceModel()
                .setAuthor("pesho@abv.bg")
                .setTextContent("I like it!")
                .setApproved(false)
                .setCreated(LocalDateTime.now());
    }

    private DietServiceModel createDietServiceModel() {
        return new DietServiceModel()
                .setId(1)
                .setTitle("Ultra Diet")
                .setDescription("Amazing!")
                .setAuthor("pesho@abv.bg")
                .setGoal(GoalEnum.LOSE_FAT);
    }

    private UserEntity createUser() {
        UserEntity userEntity = new UserEntity()
                .setFirstName("Pesho")
                .setLastName("Petrov")
                .setEmail("pesho@abv.bg")
                .setRoles(List.of(new RoleEntity().setRole(RoleEnum.USER)));

        userEntity.setId(1);

        return userEntity;
    }

    private UserEntity createAnotherUser() {
        UserEntity userEntity = new UserEntity()
                .setFirstName("Misho")
                .setLastName("Petrov")
                .setEmail("misho@abv.bg")
                .setRoles(List.of(new RoleEntity().setRole(RoleEnum.USER)));

        userEntity.setId(2);

        return userEntity;
    }

    private GoalEntity createLoseFatGoal() {
        return new GoalEntity()
                .setName(GoalEnum.LOSE_FAT);
    }

    private GoalEntity createGainMassGoal() {
        return new GoalEntity()
                .setName(GoalEnum.GAIN_MASS);
    }

    private DietEntity createDiet() {
        UserEntity author = createUser();
        GoalEntity loseFatGoal = createLoseFatGoal();

        DietEntity diet = new DietEntity()
                .setTitle("Diet 1")
                .setAuthor(author)
                .setDescription("Very healthy!")
                .setCreated(LocalDateTime.now())
                .setGoal(loseFatGoal);

        diet.setId(1);

        return diet;
    }

    private DietEntity createAnotherDiet() {
        UserEntity author = createUser();
        GoalEntity gainMassGoal = createGainMassGoal();

        DietEntity diet = new DietEntity()
                .setTitle("Diet 2")
                .setAuthor(author)
                .setDescription("With this diet you can easily gain kilos and you will see the effect in less than a month!")
                .setCreated(LocalDateTime.now())
                .setGoal(gainMassGoal);

        diet.setId(2);

        return diet;
    }

    private Page<DietEntity> createPageOfDiets() {
        DietEntity diet1 = createDiet();
        DietEntity diet2 = createAnotherDiet();
        return new PageImpl<>(List.of(diet1, diet2));
    }
}
