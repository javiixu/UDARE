package com.example.udare.data.repositories.Interfaces;

import com.example.udare.data.repositories.Implementations.PostRepository;
import com.example.udare.data.repositories.Implementations.UserRepository;

public interface IUserRepository {
    void getAllUsers(final UserRepository.callbackGetAllUsers callback);
}
