package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.CommentEntity;
import bg.softuni.fitcom.models.view.CommentViewModel;
import bg.softuni.fitcom.models.view.TrainingProgramsOverviewViewModel;
import bg.softuni.fitcom.repositories.CommentRepository;
import bg.softuni.fitcom.services.CommentService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<CommentViewModel> getPending() {
        return this.commentRepository.findAllByApproved(false)
                .stream()
                .map(this::toView)
                .toList();
    }

    @Override
    public void approve(long id) {
        CommentEntity comment = this.commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such comment."));

        comment.setApproved(true);
        this.commentRepository.save(comment);
    }

    @Override
    public void delete(long id) {
        CommentEntity comment = this.commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such comment."));

        this.commentRepository.deleteById(id);
    }

    private CommentViewModel toView(CommentEntity entity) {
        return new CommentViewModel()
                .setId(entity.getId())
                .setTextContent(entity.getTextContent())
                .setAuthor(entity.getAuthor().getFirstName() + " " + entity.getAuthor().getLastName())
                .setCreated(entity.getCreated().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
    }
}
