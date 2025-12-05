package com.internship.insighthub.controller;

import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FrontendController {

    private final UserService userService;

    public FrontendController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }


    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("user") UserRegistrationDto userDto,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(userDto);


            model.addAttribute("successMessage", "User registered successfully!");


            model.addAttribute("user", new UserRegistrationDto());

            return "register";
        } catch (IllegalArgumentException ex) {

            bindingResult.reject("registrationError", ex.getMessage());
            return "register";
        }
    }
}
