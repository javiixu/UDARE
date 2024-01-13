package com.example.udare.di;

import com.example.udare.data.remote.ApiService;
import com.example.udare.data.repositories.Implementations.ChallengeRepository;
import com.example.udare.data.repositories.Implementations.PostRepository;
import com.example.udare.data.repositories.Implementations.ReactionRepository;
import com.example.udare.data.repositories.Implementations.UserRepository;
import com.example.udare.data.repositories.Interfaces.IChallengeRepository;
import com.example.udare.data.repositories.Interfaces.IPostRepository;
import com.example.udare.data.repositories.Interfaces.IReactionRepository;
import com.example.udare.data.repositories.Interfaces.IUserRepository;
import com.example.udare.services.implementations.ChallengeService;
import com.example.udare.services.implementations.PostService;
import com.example.udare.services.implementations.ReactionService;
import com.example.udare.services.implementations.UserService;
import com.example.udare.services.interfaces.IChallengeService;
import com.example.udare.services.interfaces.IPostService;
import com.example.udare.services.interfaces.IReactionService;
import com.example.udare.services.interfaces.IUserService;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public final class AppModule {

    private static final String BASE_URL = "http://172.20.10.6:3000/";

    @Provides
    @Singleton
    public ApiService provideApiService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }

    @Provides
    @Singleton
    public IChallengeRepository provideChallengeRepository(ApiService api) {
        return new ChallengeRepository(api);
    }

    @Provides
    @Singleton
    public IPostRepository providePostRepository(ApiService api) {
        return new PostRepository(api);
    }

    @Provides
    @Singleton
    public IReactionRepository provideReactionRepository(ApiService api) {
        return new ReactionRepository(api);
    }

    @Provides
    @Singleton
    public IUserRepository provideUserRepository(ApiService api) {
        return new UserRepository(api);
    }

    @Provides
    @Singleton
    public IUserService provideUserService(IUserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Provides
    @Singleton
    public IChallengeService provideChallengeService(IChallengeRepository challengeRepository) {
        return new ChallengeService(challengeRepository);
    }

    @Provides
    @Singleton
    public IPostService providePostService(IPostRepository postRepository) {
        return new PostService(postRepository);
    }

    @Provides
    @Singleton
    public IReactionService provideReactionService(IReactionRepository reactionRepository) {
        return new ReactionService(reactionRepository);
    }


}
