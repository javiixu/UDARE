package com.example.udare.data.remote;

import com.example.udare.data.model.Post;
import com.example.udare.data.model.Challenge;
import com.example.udare.data.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/users")
    Call<List<User>> getAllUsers();

    @GET("/users/{id}")
    Call<User> getUserById(@Path("id") String userId);

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

    @PUT("/users/{id}")
    Call<User> updateUserById(
            @Path("id") String userId,
            @Body User updateUserData
    );

    @Multipart
    @PUT("users/updateImage/{id}")
    Call<User> updateUserByIdImage(
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part user,
            @Path("id") String userId
    );


}
