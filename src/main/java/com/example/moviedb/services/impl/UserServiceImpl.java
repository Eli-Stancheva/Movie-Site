package com.example.moviedb.services.impl;

import com.example.moviedb.models.DTOs.UserLoginDTO;
import com.example.moviedb.models.DTOs.UserRegistrationDTO;
import com.example.moviedb.models.enums.RoleEnum;
import com.example.moviedb.repositories.*;
import com.example.moviedb.services.EmailService;
import com.example.moviedb.services.UserService;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.util.CurrentUser;
import com.example.moviedb.util.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final WatchlistMovieRepository watchlistMovieRepository;
    private final UserRoleServiceImpl userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUser currentUser;
    private final EmailService emailService;
    private final Random random;
    private final RatingFromUserRepository ratingFromUserRepository;
    private final RatingSeriesFromUserRepository ratingSeriesFromUserRepository;
    private final CommentRepository commentRepository;
    private final WatchlistRepository watchlistRepository;
    private final WatchlistSeriesRepository watchlistSeriesRepository;
    private boolean forgotPasswordRequested = false;
    private String resetCode;

    private boolean isFirstLogin = true;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, WatchlistMovieRepository watchlistMovieRepository, UserRoleServiceImpl userRoleService, PasswordEncoder passwordEncoder, CurrentUser currentUser, EmailService emailService, Random random, RatingFromUserRepository ratingFromUserRepository, RatingSeriesFromUserRepository ratingSeriesFromUserRepository, CommentRepository commentRepository, WatchlistRepository watchlistRepository, WatchlistSeriesRepository watchlistSeriesRepository) {
        this.userRepository = userRepository;
        this.watchlistMovieRepository = watchlistMovieRepository;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
        this.currentUser = currentUser;
        this.emailService = emailService;
        this.random = random;
        this.ratingFromUserRepository = ratingFromUserRepository;
        this.ratingSeriesFromUserRepository = ratingSeriesFromUserRepository;
        this.commentRepository = commentRepository;
        this.watchlistRepository = watchlistRepository;
        this.watchlistSeriesRepository = watchlistSeriesRepository;
    }
    @Override
    public Boolean registerUser(UserRegistrationDTO userRegistrationDTO) {
        if (!userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword())) {
            return false;
        }

        if (this.userRepository.findByEmail(userRegistrationDTO.getEmail()).isPresent()) {
            return false;
        }

        User user = new User()
                .setUsername(userRegistrationDTO.getUsername())
                .setEmail(userRegistrationDTO.getEmail())
                .setPassword(this.passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .setActive(true)
                .setRole(this.userRoleService.getRole("USER"));

        this.userRepository.save(user);

        return true;
    }
    @Override
    public Boolean loginUser(UserLoginDTO userLoginDTO) {
        Optional<User> optionalUser = this.userRepository.findByEmail(userLoginDTO.getEmail());

        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        if (!this.passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return false;
        }

        this.currentUser
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setRole(user.getRole())
                .setLogged(true);

        if (forgotPasswordRequested) {
            forgotPasswordRequested = false;
        }

        return true;
    }
    @Override
    public Boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    @Override
    public void logOutUser() {
        this.currentUser.logout();
    }
    @Override
    public CurrentUser getCurrentUser() {
        return this.currentUser;
    }
    @Override
    public void updateUser(UserForm userForm) {
        CurrentUser currentUser = getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setEmail(userForm.getEmail());
        if (!userForm.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        }

        userRepository.save(user);
    }



    @Override
    public void requestPasswordReset() {
        forgotPasswordRequested = true;
        resetCode = String.valueOf(random.nextInt(90000) + 10000);
    }

    @Override
    public void sendResetPasswordCode(String email) {
        if (forgotPasswordRequested && resetCode != null) {
            emailService.sendEmail(email, "Password Reset Code", "Your reset code is: " + resetCode);
            updatePassword(email,resetCode);
            forgotPasswordRequested = false;
        } else {
            throw new RuntimeException("The password reset code was not generated.");
        }
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new RuntimeException("Потребителят с такъв email не съществува.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findByRole_Name(RoleEnum.USER);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Изтриване на свързаните рейтинги и коментари
            ratingFromUserRepository.deleteByUserId(id);
            ratingSeriesFromUserRepository.deleteByUserId(id);
            watchlistRepository.deleteByUserId(id);
            watchlistMovieRepository.deleteByUserId(id);
            watchlistSeriesRepository.deleteByUserId(id);
            commentRepository.deleteByUserId(id);

            userRepository.delete(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
