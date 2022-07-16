package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.binding.CommentAddBingingModel;
import bg.softuni.fitcom.models.binding.TrainingProgramBindingModel;
import bg.softuni.fitcom.models.enums.BodyPartEnum;
import bg.softuni.fitcom.models.service.CommentAddServiceModel;
import bg.softuni.fitcom.models.service.ExerciseAddServiceModel;
import bg.softuni.fitcom.models.service.TrainingProgramServiceModel;
import bg.softuni.fitcom.models.user.FitcomPrincipal;
import bg.softuni.fitcom.models.view.TrainingProgramDetailsViewModel;
import bg.softuni.fitcom.models.view.TrainingProgramsOverviewViewModel;
import bg.softuni.fitcom.services.TrainingProgramService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/training-programs")
public class TrainingProgramController {
    private final ModelMapper modelMapper;
    private final TrainingProgramService trainingProgramService;
    private static final List<String> EXERCISES = new ArrayList<>();

    public TrainingProgramController(ModelMapper modelMapper, TrainingProgramService trainingProgramService) {
        this.modelMapper = modelMapper;
        this.trainingProgramService = trainingProgramService;
    }

    @GetMapping
    public String getPagedTrainingPrograms(@RequestParam(value = "title", required = false) String title,
                                           @RequestParam(value = "bodyPart", required = false) String bodyPart,
                                           @RequestParam(defaultValue = "1") Integer pageNo,
                                           @RequestParam(defaultValue = "3") Integer pageSize,
                                           @RequestParam(defaultValue = "id") String sortBy,
                                           Model model) {

        if (bodyPart != null && bodyPart.equals("ALL")) {
            bodyPart = null;
        }

        int lastPage = this.trainingProgramService.getLastPageNumber(title, bodyPart, pageSize);

        addPageNumbers(model, lastPage);
        pageNo = getValidatedPageNumber(pageNo, lastPage);

        model.addAttribute("trainingPrograms",
                this.trainingProgramService.getTrainingPrograms(title, bodyPart,pageNo - 1, pageSize, sortBy));

        model.addAttribute("title", title);
        model.addAttribute("bodyPart", bodyPart);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("lastPage", lastPage);

        return "training-programs";
    }

    @GetMapping("/{id}")
    public String getTrainingProgramDetails(@PathVariable long id, Model model, Authentication auth) {
        model.addAttribute("trainingProgram", this.trainingProgramService.getTrainingProgram(id));
        model.addAttribute("canModify", this.trainingProgramService.canModify(auth.getName(), id));
        model.addAttribute("isInUserFavourites",
                this.trainingProgramService.isInUserFavourites(auth.getName(), id));
        model.addAttribute("comments", this.trainingProgramService.getComments(id));
        return "training-program-details";
    }

    @GetMapping("/add")
    public String getAddProgram(Model model) {
        model.addAttribute("trainingProgram", new TrainingProgramBindingModel());
        model.addAttribute("bodyParts", List.of(BodyPartEnum.ABS, BodyPartEnum.ARMS,
                BodyPartEnum.BACK, BodyPartEnum.CHEST, BodyPartEnum.LEGS, BodyPartEnum.SHOULDERS, BodyPartEnum.OTHER));
        return "training-programs-add";
    }

    @GetMapping("/add-errors")
    public String getAddWithErrors(Model model) {
        model.addAttribute("bodyParts", List.of(BodyPartEnum.ABS, BodyPartEnum.ARMS,
                BodyPartEnum.BACK, BodyPartEnum.CHEST, BodyPartEnum.LEGS, BodyPartEnum.SHOULDERS, BodyPartEnum.OTHER));
        return "training-programs-add";
    }

    @PostMapping("/add")
    public String addProgram(@Valid TrainingProgramBindingModel bindingModel, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, Authentication auth) {

        TrainingProgramServiceModel serviceModel = toServiceModel(bindingModel, auth);

        if (bindingResult.hasErrors() || serviceModel == null) {
            redirectAttributes.addFlashAttribute("trainingProgram", bindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.trainingProgram", bindingResult);

            return "redirect:add-errors";
        }

        this.trainingProgramService.updateProgram(serviceModel);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("@trainingProgramServiceImpl.canModify(#principal.name, #id)")
    public String getEditTrainingProgram(@PathVariable long id, Model model, Authentication principal) {

        model.addAttribute("bodyParts", List.of(BodyPartEnum.ABS, BodyPartEnum.ARMS,
                BodyPartEnum.BACK, BodyPartEnum.CHEST, BodyPartEnum.LEGS, BodyPartEnum.SHOULDERS, BodyPartEnum.OTHER));

        TrainingProgramDetailsViewModel viewModel = this.trainingProgramService.getTrainingProgram(id);

        viewModel.getExercises()
                .forEach(e -> {
                    EXERCISES.add("email|" + e.getName());
                    EXERCISES.add("description|" + e.getDescription());
                    EXERCISES.add("video|" + e.getVideoUrl());
                });

        TrainingProgramBindingModel bindingModel = this.modelMapper
                .map(viewModel, TrainingProgramBindingModel.class)
                .setExercisesData(EXERCISES);

        model.addAttribute("trainingProgram", bindingModel);
        model.addAttribute("exerciseData", EXERCISES);
        return "training-program-edit";
    }

    @GetMapping("/{id}/edit-errors")
    public String getEditWithErrors(@PathVariable long id, Model model) {
        model.addAttribute("bodyParts", List.of(BodyPartEnum.ABS, BodyPartEnum.ARMS,
                BodyPartEnum.BACK, BodyPartEnum.CHEST, BodyPartEnum.LEGS, BodyPartEnum.SHOULDERS, BodyPartEnum.OTHER));
        model.addAttribute("exerciseData", EXERCISES);

        return "training-program-edit";
    }

    @PutMapping("/{id}/edit")
    @PreAuthorize("@trainingProgramServiceImpl.canModify(#principal.name, #id)")
    public String editTrainingProgram(@PathVariable long id, Authentication principal,
                                      @Valid TrainingProgramBindingModel bindingModel,
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        TrainingProgramServiceModel serviceModel = toServiceModel(bindingModel, principal);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("trainingProgram", bindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.trainingProgram", bindingResult);

            return "redirect:edit-errors";
        }

        this.trainingProgramService.updateProgram(serviceModel);
        return "redirect:/training-programs";
    }

    @PreAuthorize("@trainingProgramServiceImpl.canModify(#principal.name, #id)")
    @DeleteMapping("/{id}")
    public String deleteTraining(@PathVariable long id, Authentication principal) {
        this.trainingProgramService.deleteProgram(id);
        return "redirect:/training-programs";
    }

    @DeleteMapping("/{id}/remove-exercise")
    @ResponseBody
    public String removeExerciseFromTraining(@PathVariable long id, @RequestBody String exerciseName) {
        this.trainingProgramService.removeExerciseFromTraining(id, exerciseName);
        return "redirect:/training-programs/" + id;
    }

    @PostMapping("/{id}/add-to-favourites")
    @PreAuthorize("!@trainingProgramServiceImpl.isInUserFavourites(#principal.name, #id)")
    public String addToFavourites(@PathVariable long id, Authentication principal) {
        this.trainingProgramService.addToFavourites(id, principal.getName());
        return "redirect:/training-programs/" + id;
    }

    @PostMapping("/{id}/remove-from-favourites")
    @PreAuthorize("@trainingProgramServiceImpl.isInUserFavourites(#principal.name, #id)")
    public String removeFromFavourites(@PathVariable long id, Authentication principal) {
        this.trainingProgramService.removeFromFavourites(id, principal.getName());
        return "redirect:/training-programs/" + id;
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
            return "redirect:/training-programs/" + id;
        }

        CommentAddServiceModel serviceModel = new CommentAddServiceModel()
                .setTextContent(bingingModel.getTextContent())
                .setAuthor(auth.getName())
                .setCreated(LocalDateTime.now());

        this.trainingProgramService.addComment(serviceModel, id);
        return "redirect:/training-programs/" + id;
    }

    private TrainingProgramServiceModel toServiceModel(TrainingProgramBindingModel bindingModel,
                                                       Authentication auth) {

        List<ExerciseAddServiceModel> exercises = getExercises(bindingModel.getExercisesData());
        if (exercises == null) return null;

        return this.modelMapper
                .map(bindingModel, TrainingProgramServiceModel.class)
                .setExercises(exercises)
                .setAuthor(auth.getName());
    }

    private List<ExerciseAddServiceModel> getExercises(List<String> exercisesData) {
        if (exercisesData == null) return null;

        List<ExerciseAddServiceModel> exercises = new ArrayList<>();

        for (int i = 0; i < exercisesData.size(); i++) {
            String name = exercisesData.get(i);
            String description = exercisesData.get(++i);
            String videoUrl = exercisesData.get(++i);

            if ("".equals(name.trim()) || "".equals(description.trim()) || "".equals(videoUrl.trim())) {
                return null;
            }

            ExerciseAddServiceModel exercise = new ExerciseAddServiceModel()
                    .setName(name)
                    .setDescription(description)
                    .setVideoUrl(videoUrl);

            exercises.add(exercise);
        }

        return exercises;
    }

    private Integer getValidatedPageNumber(Integer pageNo, int lastPage) {
        if (pageNo < 1) {
            pageNo = 1;
        } else if (pageNo > lastPage) {
            pageNo = lastPage;
        }
        return pageNo;
    }

    private void addPageNumbers(Model model, int totalPages) {
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();

            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
}
