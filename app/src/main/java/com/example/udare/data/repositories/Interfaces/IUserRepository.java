package com.example.udare.data.repositories.Interfaces;

import com.example.udare.data.repositories.Implementations.PostRepository;
import com.example.udare.data.repositories.Implementations.UserRepository;

// Import User
import com.example.udare.data.model.User;

import java.io.File;


public interface IUserRepository {
    void getAllUsers(final UserRepository.callbackGetAllUsers callback);

    void getUserById(final UserRepository.callbackGetUserById callback, int id);

    void createUser(final UserRepository.callbackPostUser callback, User user);

//    void createUser(final UserRepository.callbackPostUser callback, String user, String image);

    void updateProfilePic(final UserRepository.callbackPostUser callback, User user, File image);
}
