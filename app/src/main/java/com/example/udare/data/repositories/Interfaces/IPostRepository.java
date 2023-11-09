package com.example.udare.data.repositories.Interfaces;

import com.example.udare.data.model.Post;
import com.example.udare.data.repositories.Implementations.ChallengeRepository;
import com.example.udare.data.repositories.Implementations.PostRepository;

import java.io.File;

public interface IPostRepository {
    void getAllPosts(final PostRepository.callbackGetAllPosts callback);
    void uploadPost(File file, Post post, final PostRepository.callbackUploadPost callback);

}
