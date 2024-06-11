package com.example.moviedb.controllers;

import com.example.moviedb.models.entity.User;
import com.example.moviedb.services.UserService;
import com.example.moviedb.util.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {
    private final CurrentUser currentUser;
    private final UserService userService;

    @Autowired
    public AdminController(CurrentUser currentUser, UserService userService) {
        this.currentUser = currentUser;
        this.userService = userService;
    }

    @GetMapping("/admin-page")
    public String adminPage() {
        if(currentUser.isAdmin()) {
            return "admin-nav";
        } else {
            return "redirect:/access-denied";
        }
    }

    @GetMapping("/admin-users")
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin-users";
    }


    @PostMapping("/admin-delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin-users";
    }
}
