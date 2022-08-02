package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.services.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/pending")
    @PreAuthorize("@userServiceImpl.isAdmin(#principal.name)")
    public String getPending(Model model, Authentication principal) {
        model.addAttribute("comments", this.commentService.getPending());
        return "pending";
    }

    @PostMapping("/pending/{id}")
    @PreAuthorize("@userServiceImpl.isAdmin(#principal.name)")
    public String approve(@PathVariable long id, Authentication principal) {
        this.commentService.approve(id);
        return "redirect:/comments/pending";
    }

    @DeleteMapping("/pending/{id}")
    @PreAuthorize("@userServiceImpl.isAdmin(#principal.name)")
    public String delete(@PathVariable long id, Authentication principal) {
        this.commentService.delete(id);
        return "redirect:/comments/pending";
    }
}
