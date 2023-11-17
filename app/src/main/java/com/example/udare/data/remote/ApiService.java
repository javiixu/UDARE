package com.example.udare.data.remote;

import com.example.udare.data.model.Post;
import com.example.udare.data.model.Challenge;
import com.example.udare.data.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @GET("/users")
    Call<List<User>> getAllUsers();

//    Get User by id
    @GET("/users/{id}")
    Call<User> getUserById(int id);

//    Create User
    @Multipart
    @POST("/users/add")
    Call<User> createUser(
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part user
    );

//    Create User without image
    @POST("/users/add")
    Call<User> createUser(@Body User user);

//    Update Profile Pic User
    @Multipart
    @POST("/users/updateProfilePic")
    Call<User> updateProfilePic(
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part user
    );

    @GET("/posts")
    Call<List<Post>> getAllPosts();

    @GET("/challenges")
    Call<List<Challenge>> getAllChallenges();

    @Multipart
    @POST("/posts/add")
    Call<Post> uploadPost(
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part post
    );


}
