package com.internship.insighthub.controller;

import com.internship.insighthub.dto.UserRegistrationDto;
import com.internship.insighthub.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class FrontendController {

    private final UserService userService;

    // Главная страница – index.html
    @GetMapping("/")
    public String index() {
        return "index"; // ищет templates/index.html
    }

    // Показать форму регистрации – register.html
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // Если ещё не положили user в модель (после redirect)
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserRegistrationDto());
        }
        return "register"; // ищет templates/register.html
    }

    // Обработка формы регистрации
    @PostMapping("/register")
    public String handleRegister(
            @Valid @ModelAttribute("user") UserRegistrationDto userDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // Если есть ошибки валидации – вернём форму с ошибками
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", userDto);
            return "redirect:/register";
        }

        // Регистрация пользователя через сервис
        userService.registerUser(userDto);

        // Сообщение об успехе
        redirectAttributes.addFlashAttribute(
                "successMessage", "User registered successfully!");
        return "redirect:/register";
    }
}

