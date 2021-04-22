package com.alfonso.clientreddit.RepositoryReddit.webService

import com.alfonso.clientreddit.models.RedditListingResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RedditService {

    @GET("r/subreddit/top")
    suspend fun getTop(@Header("Authorization") auth : String,@Query("t") t : String,@Query("count") count : Int, @Query("limit") limit : Int) : RedditListingResponse
}