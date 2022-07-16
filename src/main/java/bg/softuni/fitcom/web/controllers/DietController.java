package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.binding.CommentAddBingingModel;
import bg.softuni.fitcom.models.binding.DietBindingModel;
import bg.softuni.fitcom.models.service.CommentAddServiceModel;
import bg.softuni.fitcom.models.service.DietServiceModel;
import bg.softuni.fitcom.models.view.DietDetailsViewModel;
import bg.softuni.fitcom.services.DietService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/diets")
public class DietController {
    private final DietService dietService;
    private final ModelMapper modelMapper;

    public DietController(DietService dietService, ModelMapper modelMapper) {
        this.dietService = dietService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
//    @PreAuthorize(canAccess)
    public String getDiets(Model model, Authentication auth) {
        model.addAttribute("diets", this.dietService.getDiets());
        return "diets";
    }

    @ModelAttribute("dietBindingModel")
    public DietBindingModel dietBindingModel() {
        return new DietBindingModel();
    }

    @GetMapping("/add")
    public String getAddDiet() {
        return "diets-add";
    }

    @PostMapping("/add")
    public String addDiet(@Valid DietBindingModel bindingModel, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Authentication auth) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("dietBindingModel", bindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.dietBindingModel", bindingResult);
            return "redirect:/diets/add";
        }

        DietServiceModel serviceModel = this.modelMapper.map(bindingModel, DietServiceModel.class)
                .setAuthor(auth.getName());

        this.dietService.updateDiet(serviceModel);
        return "redirect:";
    }

    @GetMapping("/{id}")
    public String getDietDetails(@PathVariable long id, Model model, Authentication auth) {
        model.addAttribute("diet", this.dietService.getDiet(id));
        model.addAttribute("canModify", this.dietService.canModify(auth.getName(), id));
        model.addAttribute("isInUserFavourites", this.dietService.isInUserFavourites(auth.getName(), id));
        model.addAttribute("comments", this.dietService.getComments(id));
        return "diet-details";
    }

    @ModelAttribute("commentBindingModel")
    public CommentAddBingingModel commentBindingModel() {
        return new CommentAddBingingModel();
    }

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable long id, Authentication auth,
                             @Valid CommentAddBingingModel bingingModel, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentBindingModel", bingingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.commentBindingModel", bindingResult);
            return "redirect:/diets/" + id;
        }

        CommentAddServiceModel serviceModel = new CommentAddServiceModel()
                .setTextContent(bingingModel.getTextContent())
                .setAuthor(auth.getName())
                .setCreated(LocalDateTime.now());

        this.dietService.addComment(serviceModel, id);
        return "redirect:/diets/" + id;
    }


    // Copied:

    @GetMapping("/{id}/edit")
    @PreAuthorize("@dietServiceImpl.canModify(#principal.username, #id)")
    public String getEditDiet(@PathVariable long id, Model model) {
        DietDetailsViewModel viewModel = this.dietService.getDiet(id);

        DietBindingModel bindingModel = this.modelMapper
                .map(viewModel, DietBindingModel.class);

        model.addAttribute("diet", bindingModel);
        return "diet-edit";
    }

    @GetMapping("/{id}/edit-errors") // TODO: do I need that?
    public String getEditWithErrors(@PathVariable long id, Model model) {
        return "diet-edit";
    }

    @PutMapping("/{id}/edit")
    public String editDiet(@PathVariable long id, Authentication auth,
                                      @Valid DietBindingModel bindingModel,
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        DietServiceModel serviceModel = this.modelMapper.map(bindingModel, DietServiceModel.class)
                .setAuthor(auth.getName());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("diet", bindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.diet", bindingResult);

            return "redirect:edit-errors";
        }

        this.dietService.updateDiet(serviceModel);
        return "redirect:/diets";
    }

    @PreAuthorize("@dietServiceImpl.canModify(#principal.username, #id)")
    @DeleteMapping("/{id}")
    public String deleteDiet(@PathVariable long id) {
        this.dietService.deleteDiet(id);
        return "redirect:/diets";
    }

    @PostMapping("/{id}/add-to-favourites")
    @PreAuthorize("!@dietServiceImpl.isInUserFavourites(#principal.username, #id)")
    public String addToFavourites(@PathVariable long id, Authentication auth) {
        this.dietService.addToFavourites(id, auth.getName());
        return "redirect:/diets/" + id;
    }

    @PostMapping("/{id}/remove-from-favourites")
    @PreAuthorize("@dietServiceImpl.isInUserFavourites(#principal.username, #id)")
    public String removeFromFavourites(@PathVariable long id, Authentication auth) {
        this.dietService.removeFromFavourites(id, auth.getName());
        return "redirect:/diets/" + id;
    }
}