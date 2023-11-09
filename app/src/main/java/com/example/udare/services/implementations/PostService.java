package com.example.udare.services.implementations;

import com.example.udare.data.model.Challenge;
import com.example.udare.data.model.Post;
import com.example.udare.data.repositories.Implementations.ChallengeRepository;
import com.example.udare.data.repositories.Implementations.PostRepository;
import com.example.udare.data.repositories.Interfaces.IChallengeRepository;
import com.example.udare.data.repositories.Interfaces.IPostRepository;
import com.example.udare.services.interfaces.IPostService;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


public class PostService implements IPostService {
    final IPostRepository postRepository;

    @Inject
    public PostService(IPostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void getAllPosts(PostRepository.callbackGetAllPosts callback) {
        postRepository.getAllPosts(new PostRepository.callbackGetAllPosts() {
            @Override
            public void onSuccess(List<Post> posts) {
                callback.onSuccess(posts);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        });
    }

    @Override
    public void uploadPost(File file, Post post, PostRepository.callbackUploadPost callback) {
        postRepository.uploadPost(file, post, new PostRepository.callbackUploadPost() {
            @Override
            public void onSuccess(Post post) {
                callback.onSuccess(post);
            }

            @Override
            public void onError(String mensajeError) {
                callback.onError(mensajeError);
            }
        });
    }
}
