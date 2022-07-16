package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ErrorController {
    private final UserService userService;

    public ErrorController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("forbidden")
    public String unauthorized() {
        return "403";
    }

    @GetMapping("/training-programs/add/forbidden")
    public String getErrorForTrainer(Model model, Authentication auth) {
        model.addAttribute("neededRole", "trainer");
        model.addAttribute("hasApplied", this.userService.hasApplied(auth.getName(), RoleEnum.TRAINER));

        return "not-allowed";
    }

    @GetMapping("/diets/add/forbidden")
    public String getErrorForNutritionist(Model model, Authentication auth) {
        model.addAttribute("neededRole", "nutritionist");
        model.addAttribute("hasApplied", this.userService.hasApplied(auth.getName(), RoleEnum.NUTRITIONIST));

        return "not-allowed";
    }
}
