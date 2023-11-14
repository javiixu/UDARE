package com.example.udare.services.interfaces;

import com.example.udare.data.model.User;
import com.example.udare.data.repositories.Implementations.ChallengeRepository;
import com.example.udare.data.repositories.Implementations.UserRepository;

public interface IUserService {
    void getAllUsers(UserRepository.callbackGetAllUsers callback);

    void updateUser(String userId, User updatedUser , UserRepository.callbackUpdateUser callback);

    void getUserById(String userId, UserRepository.callbackGetUserById callback);
}
