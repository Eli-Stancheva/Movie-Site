package com.example.moviedb.services;

import com.example.moviedb.models.DTOs.UserLoginDTO;
import com.example.moviedb.models.DTOs.UserRegistrationDTO;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.util.CurrentUser;
import com.example.moviedb.util.UserForm;

public interface UserService {
    Boolean registerUser(UserRegistrationDTO userRegistrationDTO);

    Boolean loginUser(UserLoginDTO userLoginDTO);

    void logOutUser();
    Boolean isEmailExists(String email);
    CurrentUser getCurrentUser();

    void updateUser(UserForm userForm);

    void addToWatchlist(String username, Movie movie);
    void requestPasswordReset();
    void updatePassword(String email, String newPassword);

    void sendResetPasswordCode(String email);
}
