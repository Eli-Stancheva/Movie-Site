package com.example.moviedb.controllers;

import com.example.moviedb.util.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private CurrentUser currentUser;

    @Autowired
    public AdminController(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @GetMapping("/admin-page")
    public String adminPage() {
        if(currentUser.isAdmin()) {
            return "admin-nav";
        } else {
            return "redirect:/access-denied";
        }
    }
}
