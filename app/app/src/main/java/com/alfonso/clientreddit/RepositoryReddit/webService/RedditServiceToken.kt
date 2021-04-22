package com.alfonso.clientreddit.RepositoryReddit.webService

import com.alfonso.clientreddit.models.AccessToken
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RedditServiceToken {
    @FormUrlEncoded
    @POST("access_token")
    suspend fun getAccessToken(@Header("Authorization") auth : String,@Field("grant_type") grantType : String, @Field("device_id") deviceId : String ) : AccessToken
}