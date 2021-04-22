package com.alfonso.clientreddit.repositoryReddit.webService

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object RedditModule {
    @Singleton
    @Provides
    fun provideRedditServiceToken() : RedditServiceToken {
        val moshi  = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder().baseUrl("https://www.reddit.com/api/v1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(RedditServiceToken::class.java)
    }
    @Singleton
    @Provides
    fun provideRedditService() : RedditService {
        val moshi  = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder().baseUrl("https://oauth.reddit.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(RedditService::class.java)
    }

}