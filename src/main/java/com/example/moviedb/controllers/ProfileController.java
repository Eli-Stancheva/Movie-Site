package com.example.moviedb.controllers;

import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.services.MoviesService;
import com.example.moviedb.services.TVSeriesService;
import com.example.moviedb.services.UserService;
import com.example.moviedb.util.CurrentUser;
import com.example.moviedb.util.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;
    private final MoviesService moviesService;
    private final TVSeriesService tvSeriesService;
    private final AuthController authController;

    @Autowired
    public ProfileController(UserService userService, MoviesService moviesService, TVSeriesService tvSeriesService, AuthController authController) {
        this.userService = userService;
        this.moviesService = moviesService;
        this.tvSeriesService = tvSeriesService;
        this.authController = authController;
    }

    @GetMapping
    public String getUserInfo(Model model) {
        CurrentUser currentUser = userService.getCurrentUser();
        UserForm userForm = new UserForm();
        userForm.setUsername(currentUser.getUsername());
        userForm.setEmail(currentUser.getEmail());

        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("email", currentUser.getEmail());
        model.addAttribute("userForm", userForm);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult, Model model,  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "redirect:/profile";
        }

        CurrentUser currentUser = userService.getCurrentUser();
        userForm.setUsername(currentUser.getUsername());

        userService.updateUser(userForm);

        userForm.setEmail("");
        userForm.setPassword("");
        userForm.setConfirmPassword("");

        model.addAttribute("successMessage", "Success! Your profile information has been updated successfully.");

        return authController.logout();
    }

    @GetMapping("/ratings")
    public String showUserRatings(Model model) {
        CurrentUser currentUser = userService.getCurrentUser();
        User user = new User();
        user.setId(currentUser.getId());

        List<Movie> userMovieRatings = moviesService.getUserRatings(user);
        List<TVSeries> userSeriesRatings = tvSeriesService.getUserRatings(user);

        model.addAttribute("userMovieRatings", userMovieRatings);
        model.addAttribute("userSeriesRatings", userSeriesRatings);

        return "user-ratings";
    }
}
