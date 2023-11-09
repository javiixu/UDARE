package com.example.udare.services.implementations;

import com.example.udare.data.model.Challenge;
import com.example.udare.data.model.User;
import com.example.udare.data.repositories.Implementations.ChallengeRepository;
import com.example.udare.data.repositories.Implementations.UserRepository;
import com.example.udare.data.repositories.Interfaces.IPostRepository;
import com.example.udare.data.repositories.Interfaces.IUserRepository;
import com.example.udare.services.interfaces.IUserService;

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
}
