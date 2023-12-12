package com.example.udare.data.repositories.Interfaces;

import com.example.udare.data.model.User;
import com.example.udare.data.repositories.Implementations.PostRepository;
import com.example.udare.data.repositories.Implementations.UserRepository;
import com.example.udare.data.model.User;

import java.io.File;


public interface IUserRepository {
    void getAllUsers(final UserRepository.callbackGetAllUsers callback);

    void createUser(final UserRepository.callbackPostUser callback, User user);

//    void createUser(final UserRepository.callbackPostUser callback, String user, String image);

    void updateProfilePic(final UserRepository.callbackPostUser callback, User user, File image);


    void updateUser(String userId, User updatedUser, final UserRepository.callbackUpdateUser callback);

    void getUserById(String userId, final UserRepository.callbackGetUserById callback);

    void getUserByUid(String uid, final UserRepository.callbackGetUserByUid callback);

    void updateUserImage(File file, User user, String userId, final UserRepository.callbackUpdateUserImage callback);

    void getFollowing(String userId,final UserRepository.callbackGetFollowing callback);

    void getFollowers(String userId,final UserRepository.callbackGetFollowers callback);
    void getNotFollowingUsers(String userId,final UserRepository.callbackGetNotFollowingUsers callback);

    void followUser(String userId, String userToFollow, final UserRepository.callbackFollowUser callback);

    void unfollowUser(String userId, String userToUnfollowId, final UserRepository.callbackUnfollowUser callback);
}
