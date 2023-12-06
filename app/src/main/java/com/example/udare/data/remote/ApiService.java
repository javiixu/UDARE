package com.example.udare.data.remote;

import com.example.udare.data.model.CommentData;
import com.example.udare.data.model.Post;
import com.example.udare.data.model.Challenge;
import com.example.udare.data.model.User;
import com.example.udare.data.model.Reaction;

import java.util.List;

import okhttp3.MultipartBody;
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

    @GET("/users/{id}")
    Call<User> getUserById(@Path("id") String userId);

    @GET("/users/getUserByUid/{uid}")
    Call<User> getUserByUid(@Path("uid") String uid);

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



    @POST("/posts/{postId}/addComment")
    Call<Post> addComment(
            @Path("postId") String postId,
            @Body CommentData commentData
    );

    @GET("/users/{id}/getFollowersOfUser")
    Call<List<User>> getFollowers(@Path("id") String userId);

    @GET("/users/{id}/getFollowingOfUser")
    Call<List<User>> getFollowing(@Path("id") String userId);

//    Reactions
    @GET("/reactions/{userId}/{postId}")
    Call<List<Reaction>> getReactions(@Path("userId") String userId, @Path("postId") String postId);

    @GET("/reactions/{postId}")
    Call<List<Reaction>> getReactions(@Path("postId") String postId);

    @POST("/reactions/addWithouImage")
    Call<Reaction> addReaction(@Body Reaction reaction);

    @PUT("/reactions/{reactionId}")
    Call<Reaction> updateReaction(@Path("reactionId") String reactionId, @Body Reaction reaction);

    @PUT("/reactions/{reactionId}/{userId/delete")
    Call<Reaction> deleteReaction(@Path("reactionId") String reactionId, @Path("userId") String userId);

    @Multipart
    @POST("/reactions/add")
    Call<Reaction> uploadReaction(
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part reaction
    );

    @GET("/reactions/{userId}/{postId}/getReaction")
    Call<Reaction> getReaction(@Path("userId") String userId, @Path("postId") String postId);

//    Get reactions by post
    @GET("/reactions/post/{postId}")
    Call<List<Reaction>> getReactionsByPost(@Path("postId") String postId);

    @GET("/challenges/{id}")
    Call<Challenge> getChallengeById(@Path("id") String challengeId);

    @GET("/users/{id}/getNotFollowingUsers")
    Call<List<User>> getNotFollowingUsers(@Path("id") String userId);

}



