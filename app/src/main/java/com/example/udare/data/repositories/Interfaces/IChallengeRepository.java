package com.example.udare.data.repositories.Interfaces;

import com.example.udare.data.model.Post;
import com.example.udare.data.repositories.Implementations.ChallengeRepository;

import java.io.File;

public interface IChallengeRepository {
    void getAllChallenges(final ChallengeRepository.ChallengeCallback callback);
}
