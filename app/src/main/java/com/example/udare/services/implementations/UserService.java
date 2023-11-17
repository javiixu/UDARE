package com.example.udare.services.implementations;

import com.example.udare.data.model.User;
import com.example.udare.data.repositories.Implementations.UserRepository;
import com.example.udare.data.repositories.Interfaces.IUserRepository;
import com.example.udare.services.interfaces.IUserService;

import java.io.File;
import java.util.List;

import javax.inject.Inject;


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
    public void getUserById(UserRepository.callbackGetUserById callback, int id) {
        userRepository.getUserById(new UserRepository.callbackGetUserById() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        }, id);
    }

    @Override
    public void createUser(UserRepository.callbackPostUser callback, User user) {
        userRepository.createUser(new UserRepository.callbackPostUser() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        }, user);
    }

    @Override
    public void updateProfilePic(UserRepository.callbackPostUser callback, User user, File file) {
        userRepository.updateProfilePic(new UserRepository.callbackPostUser() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        }, user, file);
    }

}
