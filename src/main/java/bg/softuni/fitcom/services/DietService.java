package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.service.CommentAddServiceModel;
import bg.softuni.fitcom.models.service.DietServiceModel;
import bg.softuni.fitcom.models.view.CommentViewModel;
import bg.softuni.fitcom.models.view.DietDetailsViewModel;
import bg.softuni.fitcom.models.view.DietOverviewViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DietService {
    Page<DietOverviewViewModel> getDiets(Pageable pageable);

    DietServiceModel updateDiet(DietServiceModel serviceModel);

    DietDetailsViewModel getDiet(long id);

    boolean canModify(String email, long dietId);

    boolean isInUserFavourites(String email, long dietId);

    void deleteDiet(long id);

    void addToFavourites(long id, String userEmail);

    void removeFromFavourites(long id, String userEmail);

    CommentAddServiceModel addComment(CommentAddServiceModel serviceModel, long id);

    List<CommentViewModel> getComments(long id);
}
