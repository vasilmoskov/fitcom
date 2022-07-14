package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.binding.ProfileEditBindingModel;
import bg.softuni.fitcom.models.service.CloudinaryImageServiceModel;
import bg.softuni.fitcom.models.service.ProfileEditServiceModel;
import bg.softuni.fitcom.models.user.FitcomUserDetails;
import bg.softuni.fitcom.models.view.ProfileViewModel;
import bg.softuni.fitcom.models.view.TrainingProgramDetailsViewModel;
import bg.softuni.fitcom.services.CloudinaryService;
import bg.softuni.fitcom.services.TrainingProgramService;
import bg.softuni.fitcom.services.UserService;
import bg.softuni.fitcom.social.FitcomPrincipal;
import bg.softuni.fitcom.web.interceptors.ViewsInterceptor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
//@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final TrainingProgramService trainingProgramService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    private final ViewsInterceptor viewsInterceptor;

    public UserController(UserService userService, TrainingProgramService trainingProgramService,
                          CloudinaryService cloudinaryService, ModelMapper modelMapper,
                          ViewsInterceptor viewsInterceptor) {
        this.userService = userService;
        this.trainingProgramService = trainingProgramService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
        this.viewsInterceptor = viewsInterceptor;
    }

    @GetMapping("/profile")
    public String getProfile(Authentication auth, Model model) {
        model.addAttribute("userModel", this.userService.getProfile(auth.getName()));
        return "profile";
    }

    @ModelAttribute("userModel")
    public ProfileEditBindingModel userModel() {
        return new ProfileEditBindingModel();
    }

    @GetMapping("/profile/edit")
    public String getEdit(Model model, Authentication auth) {
        ProfileViewModel viewModel = this.userService.getProfile(auth.getName());
        ProfileEditBindingModel bindingModel = this.modelMapper.map(viewModel, ProfileEditBindingModel.class);
        model.addAttribute("userModel", bindingModel);

        return "profile-edit";
    }

    @GetMapping("/profile/edit-errors")
    public String getEditWithErrors() {
        return "profile-edit";
    }

    @PutMapping("/profile/edit")
    public String edit(@Valid ProfileEditBindingModel bindingModel, BindingResult bindingResult,
                       RedirectAttributes redirectAttributes, Authentication auth) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userModel", bindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.userModel", bindingResult);
            return "redirect:edit-errors";
        }

        ProfileEditServiceModel serviceModel = this.modelMapper.map(bindingModel, ProfileEditServiceModel.class)
                .setEmail(auth.getName());

        if (!bindingModel.getAvatar().isEmpty()) {
            CloudinaryImageServiceModel upload = cloudinaryService.upload(bindingModel.getAvatar());

            serviceModel
                    .setPictureUrl(upload.getUrl())
                    .setPicturePublicId(upload.getPublicId());
        }

        this.userService.editProfile(serviceModel);
        return "redirect:/profile";
    }

    @GetMapping("/favourites")
    public String getFavourites(Authentication auth, Model model) {
        model.addAttribute("trainingPrograms",
                this.userService.getFavouriteTrainingPrograms(auth.getName()));
        model.addAttribute("diets", this.userService.getFavouriteDiets(auth.getName()));
        return "favourites";
    }

    @GetMapping("/posts")
    public String getPosts(Model model, Authentication auth) {
        model.addAttribute("trainingPrograms", this.userService.getPostedTrainingPrograms(auth.getName()));
        model.addAttribute("diets", this.userService.getPostedDiets(auth.getName()));
        return "posts";
    }

    @PostMapping("/apply/nutritionist")
    public String applyForNutritionist(Authentication auth) {
        this.userService.applyForNutritionist(auth.getName());
        return "redirect:/";
    }

    @PostMapping("/apply/trainer")
    public String applyForTrainer(Authentication auth) {
        this.userService.applyForTrainer(auth.getName());
        return "redirect:/";
    }

    @GetMapping("/pending/applications")
    @PreAuthorize("@userServiceImpl.isAdmin(#principal.username)")
    public String getPendingApplications(Model model) {
        model.addAttribute("applications", this.userService.getApplications());
        return "applications";
    }

    @PostMapping("/pending/applications/{userId}")
    @PreAuthorize("@userServiceImpl.isAdmin(#principal.username)")
    public String approve(@PathVariable long userId) {
        this.userService.approveApplication(userId);
        return "redirect:/pending/applications";
    }

    @DeleteMapping("/pending/applications/{userId}")
    @PreAuthorize("@userServiceImpl.isAdmin(#principal.username)")
    public String delete(@PathVariable long userId) {
        this.userService.deleteApplication(userId);
        return "redirect:/pending/applications";
    }

    @PreAuthorize("@userServiceImpl.isAdmin(#principal.username)")
    @GetMapping("/stats")
    public String visitedUrls(Model model) {
        Map<Long, Integer> trainingProgramIdsToViews = viewsInterceptor.getTrainingProgramIdsToViews();

        Map<TrainingProgramDetailsViewModel, Integer> trainingProgramsToViews = new HashMap<>();

        for (var entry : trainingProgramIdsToViews.entrySet()) {
            long id = entry.getKey();
            TrainingProgramDetailsViewModel trainingProgram = trainingProgramService.getTrainingProgram(id);
            trainingProgramsToViews.put(trainingProgram, entry.getValue());
        }

        model.addAttribute("trainingProgramsToViews", trainingProgramsToViews);

        return "stats";
    }
}