package com.example.tawuniya.di

import android.content.Context
import com.example.tawuniya.data.local.LikedUsersDataStore
import com.example.tawuniya.data.remote.ApiService
import com.example.tawuniya.data.repository.UserRepositoryImpl
import com.example.tawuniya.domain.repos.UserRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideLikedUsersDataStore(
        @ApplicationContext context: Context,
        gson: Gson
    ): LikedUsersDataStore {
        return LikedUsersDataStore(context, gson)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: ApiService,
        likedUsersDataStore: LikedUsersDataStore
    ): UserRepository {
        return UserRepositoryImpl(apiService, likedUsersDataStore)
    }
}
