package com.example.udare.services.interfaces;

import com.example.udare.data.model.Post;
import com.example.udare.data.repositories.Implementations.PostRepository;

import java.io.File;

public interface IPostService {
    void getAllPosts(PostRepository.callbackGetAllPosts callback);
    void uploadPost(File file, Post post, PostRepository.callbackUploadPost callback);
}
