package com.example.udare.services.implementations;

import com.example.udare.data.model.Challenge;
import com.example.udare.data.repositories.Implementations.ChallengeRepository;
import com.example.udare.data.repositories.Interfaces.IChallengeRepository;
import com.example.udare.services.interfaces.IChallengeService;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


public class ChallengeService implements IChallengeService {
    final IChallengeRepository challengeRepository;

    @Inject
    public ChallengeService(IChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    @Override
    public void getAllChallenges(ChallengeRepository.ChallengeCallback callback) {
        challengeRepository.getAllChallenges(new ChallengeRepository.ChallengeCallback() {
            @Override
            public void onSuccess(List<Challenge> challenges) {
                callback.onSuccess(challenges);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        });
    }
}
