package com.example.udare.services.interfaces;

import com.example.udare.data.model.Challenge;
import com.example.udare.data.repositories.Implementations.ChallengeRepository;
import com.example.udare.data.repositories.Implementations.UserRepository;

import java.util.List;


public interface IChallengeService {
    void getAllChallenges(ChallengeRepository.ChallengeCallback callback);

    void getChallengeById(String challengeId, ChallengeRepository.callbackGetChallengeById callback);

}
