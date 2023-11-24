package com.example.udare.services.interfaces;

import com.example.udare.data.model.Reaction;
import com.example.udare.data.repositories.Implementations.ReactionRepository;

import java.io.File;

public interface IReactionService {
    void addReaction(String userID, String postID, String reaction, ReactionRepository.callbackAddReaction callback);
    void deleteReaction(String userID, String postID, ReactionRepository.callbackDeleteReaction callback);
    void getReactions(String postID, ReactionRepository.callbackGetReactions callback);
    void uploadReaction(File file, Reaction reaction, ReactionRepository.callbackUploadReaction callback);

    void getReactionsByPost(String postID, ReactionRepository.callbackGetReactionsByPost callback);

//    void addReaction(String userID, String postID, String reaction, ReactionRepository.callbackAddReaction callback);
}
