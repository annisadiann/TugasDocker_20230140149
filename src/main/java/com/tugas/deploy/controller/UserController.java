package com.tugas.deploy.controller;

import com.tugas.deploy.model.User;
import com.tugas.deploy.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // LOGIN
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session, Model model) {

        if ("admin".equals(username) && "20230140149".equals(password)) {
            session.setAttribute("loggedIn", true);
            return "redirect:/home";
        }
        model.addAttribute("error", "Username atau password salah!");
        return "login";
    }

    // HOME
    @GetMapping("/home")
    public String homePage(HttpSession session, Model model) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/";
        }
        model.addAttribute("users", userService.getAllUsers());
        return "home";
    }

    // FORM
    @GetMapping("/form")
    public String formPage(HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/";
        }
        return "form";
    }

    @PostMapping("/form")
    public String submitForm(@RequestParam String nama,
                             @RequestParam String nim,
                             @RequestParam String jenisKelamin,
                             HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/";
        }


        User newUser = User.builder()
                .nama(nama)
                .nim(nim)
                .jenis_kelamin(jenisKelamin)
                .build();

        userService.addUser(newUser);
        return "redirect:/home";
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
