package com.example.moviedb.services;

import com.example.moviedb.models.DTOs.UserLoginDTO;
import com.example.moviedb.models.DTOs.UserRegistrationDTO;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.User;
import com.example.moviedb.util.CurrentUser;
import com.example.moviedb.util.UserForm;

import java.util.List;

public interface UserService {
    Boolean registerUser(UserRegistrationDTO userRegistrationDTO);
    Boolean loginUser(UserLoginDTO userLoginDTO);
    void logOutUser();
    Boolean isEmailExists(String email);
    void updateUser(UserForm userForm);
    void requestPasswordReset();
    void updatePassword(String email, String newPassword);
    void sendResetPasswordCode(String email);
    List<User> findAllUsers();
    CurrentUser getCurrentUser();
    void deleteUser(Long id);


    //void addToWatchlist(String username, Movie movie);
    //User getCurrentUserEntity();
}
