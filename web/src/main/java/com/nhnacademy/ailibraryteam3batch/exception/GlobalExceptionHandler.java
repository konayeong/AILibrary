package com.nhnacademy.ailibraryteam3batch.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public String handleBookNotFound(BookNotFoundException e,
                                     Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }
}

