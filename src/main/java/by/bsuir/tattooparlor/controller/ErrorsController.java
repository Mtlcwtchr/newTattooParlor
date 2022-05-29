package by.bsuir.tattooparlor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
@Controller
public class ErrorsController {

    @RequestMapping("/oops")
    public String proceedOops() {
        return "oops";
    }

    @RequestMapping("/goFu")
    public String proceedHackerman() {
        return "hackerman";
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public String handleExceptions() {
        return "redirect:/oops";
    }

}
