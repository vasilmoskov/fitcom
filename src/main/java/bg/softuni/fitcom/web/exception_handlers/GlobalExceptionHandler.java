package bg.softuni.fitcom.web.exception_handlers;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.exceptions.TokenExpiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFound(ResourceNotFoundException e) {
        return new ModelAndView("404","message", e.getMessage());
//        ModelAndView mv = new ModelAndView();
//        mv.addObject();
//        mv.setViewName("404");
//        return mv;
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ModelAndView handleTokenExpiredException(TokenExpiredException e) {
        return new ModelAndView("token-expired", "message", e.getMessage());
    }
}
