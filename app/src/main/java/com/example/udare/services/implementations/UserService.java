package com.example.udare.services.implementations;


import com.example.udare.data.model.Challenge;
import com.example.udare.data.model.Post;
import com.example.udare.data.model.User;
import com.example.udare.data.repositories.Implementations.ChallengeRepository;
import com.example.udare.data.repositories.Implementations.PostRepository;
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

    @Override
    public void updateProfilePic(UserRepository.callbackPostUser callback, User user, File file) {
        userRepository.updateProfilePic(new UserRepository.callbackPostUser() {
        });
    }
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
        }, user, file);
    }


    @Override
    public void getFollowers(String userId,UserRepository.callbackGetFollowers callback) {
        userRepository.getFollowers(userId,new UserRepository.callbackGetFollowers() {
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
    public void getFollowing(String userId, UserRepository.callbackGetFollowing callback) {
        userRepository.getFollowing(userId,new UserRepository.callbackGetFollowing() {
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






