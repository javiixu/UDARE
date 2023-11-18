package com.example.udare.services.implementations;

import com.example.udare.data.model.Challenge;
import com.example.udare.data.model.Post;
import com.example.udare.data.model.User;
import com.example.udare.data.repositories.Implementations.ChallengeRepository;
import com.example.udare.data.repositories.Implementations.PostRepository;
import com.example.udare.data.repositories.Implementations.UserRepository;
import com.example.udare.data.repositories.Interfaces.IPostRepository;
import com.example.udare.data.repositories.Interfaces.IUserRepository;
import com.example.udare.services.interfaces.IUserService;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


public class UserService implements IUserService {
    final IUserRepository userRepository;

    @Inject
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getAllUsers(UserRepository.callbackGetAllUsers callback) {
        userRepository.getAllUsers(new UserRepository.callbackGetAllUsers() {
            @Override
            public void onSuccess(List<User> users) {
                callback.onSuccess(users);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        });
    }


    @Override
    public void updateUser(String userId, User updatedUser , UserRepository.callbackUpdateUser callback) {
        userRepository.updateUser(userId, updatedUser, new UserRepository.callbackUpdateUser() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        });
    }

    @Override
    public void getUserById(String userId, UserRepository.callbackGetUserById callback) {
        userRepository.getUserById(userId,new UserRepository.callbackGetUserById() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        });
    }


    @Override
    public void updateUserImage(File file, User user,String userId, UserRepository.callbackUpdateUserImage callback) {
        userRepository.updateUserImage(file, user, userId, new UserRepository.callbackUpdateUserImage() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        });
    }
}






