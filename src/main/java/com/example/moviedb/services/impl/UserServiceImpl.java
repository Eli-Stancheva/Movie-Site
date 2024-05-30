package com.example.moviedb.services.impl;

import com.example.moviedb.models.DTOs.UserLoginDTO;
import com.example.moviedb.models.DTOs.UserRegistrationDTO;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.WatchlistMovie;
import com.example.moviedb.repositories.WatchlistMovieRepository;
import com.example.moviedb.services.EmailService;
import com.example.moviedb.services.UserService;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.repositories.UserRepository;
import com.example.moviedb.util.CurrentUser;
import com.example.moviedb.util.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final WatchlistMovieRepository watchlistRepository;
    private final UserRoleServiceImpl userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUser currentUser;
    private EmailService emailService;
    private Random random;
    private boolean forgotPasswordRequested = false;
    private String resetCode;

    private boolean isFirstLogin = true;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, WatchlistMovieRepository watchlistRepository, UserRoleServiceImpl userRoleService, PasswordEncoder passwordEncoder, CurrentUser currentUser, EmailService emailService, Random random) {
        this.userRepository = userRepository;
        this.watchlistRepository = watchlistRepository;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
        this.currentUser = currentUser;
        this.emailService = emailService;
        this.random = random;
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

        if (currentUser != null) {
            User user = userRepository.findById(currentUser.getId()).orElse(null);
            if (user != null) {
                user.setUsername(userForm.getUsername());
                user.setEmail(userForm.getEmail());

                if (!userForm.getPassword().isEmpty()) {
                    user.setPassword(passwordEncoder.encode(userForm.getPassword()));
                }

                userRepository.save(user);
            }
        }
    }

    @Override
    public void addToWatchlist(String username, Movie movie) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found with username: " + username));

//        Optional<User> user = this.userRepository.findByUsername(username);
        WatchlistMovie watchlist = new WatchlistMovie();
        watchlist.setUser(user);
        watchlist.setMovie(movie);


        watchlistRepository.save(watchlist);
    }

    @Override
    public void requestPasswordReset() {
        forgotPasswordRequested = true;
        resetCode = String.valueOf(random.nextInt(90000) + 10000);
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
    public void sendResetPasswordCode(String email) {

        if (forgotPasswordRequested && resetCode != null) {
            emailService.sendEmail(email, "Password Reset Code", "Your reset code is: " + resetCode);
            updatePassword(email,resetCode);
            forgotPasswordRequested = false;
        } else {
            throw new RuntimeException("The password reset code was not generated.");
        }
    }
}
