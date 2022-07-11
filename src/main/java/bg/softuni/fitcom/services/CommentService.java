package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.view.CommentViewModel;

import java.util.List;

public interface CommentService {
    List<CommentViewModel> getPending();

    void approve(long id);

    void delete(long id);
}
