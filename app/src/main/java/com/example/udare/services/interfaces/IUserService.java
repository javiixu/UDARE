package com.example.udare.services.interfaces;

import com.example.udare.data.model.User;
import com.example.udare.data.repositories.Implementations.ChallengeRepository;
import com.example.udare.data.repositories.Implementations.UserRepository;

import java.io.File;

public interface IUserService {
    void getAllUsers(UserRepository.callbackGetAllUsers callback);

    void getUserById(UserRepository.callbackGetUserById callback, int id);

    void createUser(UserRepository.callbackPostUser callback, User user);

    void updateProfilePic(UserRepository.callbackPostUser callback, User user, File file);
}
