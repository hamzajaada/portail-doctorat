package ma.emsi.doctorat.portaildoctorat1.controllers;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.UserDTO;
import ma.emsi.doctorat.portaildoctorat1.services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register"; // templates/register.html
    }

    @PostMapping("/register")
    public String registerDoctorant(@ModelAttribute UserDTO userDTO, Model model) {
        try {
            authService.registerDoctorant(userDTO);
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
