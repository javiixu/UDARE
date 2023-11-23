package com.example.udare.data.repositories.Interfaces;

import com.example.udare.data.model.Reaction;
import com.example.udare.data.repositories.Implementations.ReactionRepository;

import java.io.File;

public interface IReactionRepository {
    void addReaction(String userID, String postID, String reaction, final ReactionRepository.callbackAddReaction callback);
    void deleteReaction(String userID, String postID, final ReactionRepository.callbackDeleteReaction callback);
    void getReactions(String postID, final ReactionRepository.callbackGetReactions callback);
    void uploadReaction(File file, Reaction reaction, final ReactionRepository.callbackUploadReaction callback);

    void getReactionsByPost(String postID, final ReactionRepository.callbackGetReactions callback);
}
