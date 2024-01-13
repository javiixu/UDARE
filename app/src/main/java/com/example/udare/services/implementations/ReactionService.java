package com.example.udare.services.implementations;

import com.example.udare.data.model.Reaction;
import com.example.udare.data.repositories.Implementations.ReactionRepository;
import com.example.udare.data.repositories.Interfaces.IReactionRepository;
import com.example.udare.services.interfaces.IReactionService;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

public class ReactionService implements IReactionService {

    final IReactionRepository reactionRepository;

    @Inject
    public ReactionService(IReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @Override
    public void addReaction(String userID, String postID, String reaction, ReactionRepository.callbackAddReaction callback) {
        reactionRepository.addReaction(userID, postID, reaction, new ReactionRepository.callbackAddReaction() {
            @Override
            public void onSuccess(Reaction reaction) {
                callback.onSuccess(reaction);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        });
    }

    @Override
    public void deleteReaction(String userID, String postID, ReactionRepository.callbackDeleteReaction callback) {
        reactionRepository.deleteReaction(userID, postID, new ReactionRepository.callbackDeleteReaction() {
            @Override
            public void onSuccess(Reaction reaction) {
                callback.onSuccess(reaction);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        });
    }

    @Override
    public void getReactions(String postID, ReactionRepository.callbackGetReactions callback) {
        reactionRepository.getReactions(postID, new ReactionRepository.callbackGetReactions() {
            @Override
            public void onSuccess(List<Reaction> reactions) {
                callback.onSuccess(reactions);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        });
    }

    @Override
    public void uploadReaction(File file, Reaction reaction, ReactionRepository.callbackUploadReaction callback) {
        reactionRepository.uploadReaction(file, reaction, new ReactionRepository.callbackUploadReaction() {
            @Override
            public void onSuccess(Reaction reaction) {
                callback.onSuccess(reaction);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        });
    }

    @Override
    public void getReactionsByPost(String postID, ReactionRepository.callbackGetReactionsByPost callback) {
        reactionRepository.getReactionsByPost(postID, new ReactionRepository.callbackGetReactions() {
            @Override
            public void onSuccess(List<Reaction> reactions) {
                callback.onSuccess(reactions);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        });
    }

}
