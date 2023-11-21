package com.example.udare.services.interfaces;

import com.example.udare.data.model.User;
import com.example.udare.data.repositories.Implementations.ChallengeRepository;
import com.example.udare.data.repositories.Implementations.UserRepository;

import java.io.File;

public interface IUserService {
    void getAllUsers(UserRepository.callbackGetAllUsers callback);

    void createUser(UserRepository.callbackPostUser callback, User user);

    void getUserByUid(String userId, UserRepository.callbackGetUserByUid callback);

    void updateProfilePic(UserRepository.callbackPostUser callback, User user, File file);

    void updateUser(String userId, User updatedUser , UserRepository.callbackUpdateUser callback);

    void getUserById(String userId, UserRepository.callbackGetUserById callback);

    void updateUserImage(File file, User user,String userId, UserRepository.callbackUpdateUserImage callback);
    void getFollowers(String userId, UserRepository.callbackGetFollowers callback);

    void getFollowing(String userId, UserRepository.callbackGetFollowing callback);

}
