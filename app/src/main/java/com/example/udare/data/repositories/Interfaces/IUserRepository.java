package com.example.udare.data.repositories.Interfaces;

import com.example.udare.data.model.User;
import com.example.udare.data.repositories.Implementations.PostRepository;
import com.example.udare.data.repositories.Implementations.UserRepository;

public interface IUserRepository {
    void getAllUsers(final UserRepository.callbackGetAllUsers callback);

    void updateUser(String userId, User updatedUser, final UserRepository.callbackUpdateUser callback);

    void getUserById(String userId, final UserRepository.callbackGetUserById callback);
}
