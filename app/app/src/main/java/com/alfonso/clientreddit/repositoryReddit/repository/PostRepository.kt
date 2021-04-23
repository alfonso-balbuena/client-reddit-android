package com.alfonso.clientreddit.repositoryReddit.repository

import androidx.lifecycle.LiveData
import com.alfonso.clientreddit.BuildConfig
import com.alfonso.clientreddit.models.AccessToken
import com.alfonso.clientreddit.models.DataPost
import com.alfonso.clientreddit.repositoryReddit.room.AppDataBase
import com.alfonso.clientreddit.repositoryReddit.webService.RedditService
import com.alfonso.clientreddit.repositoryReddit.webService.RedditServiceToken

import okhttp3.Credentials
import java.util.*
import javax.inject.Inject

class PostRepository @Inject constructor(private val redditService : RedditServiceToken, private val service : RedditService, private val dataBase : AppDataBase) {

    private val GRANT_TYPE = "https://oauth.reddit.com/grants/installed_client"
    private val T_REDDIT = "month"
    private val LIMIT = 50

    val posts : LiveData<List<DataPost>> = dataBase.postDao().getPosts(false)

    suspend fun init() {
        val list = dataBase.postDao().getPostsSuspend(false)
        if (list.isEmpty()) {
            refresh()
        }
    }

    suspend fun getToken() : AccessToken {
        val tokens = dataBase.tokenDao().getAllSynchronous()
        val deviceId = if (tokens.isEmpty()) UUID.randomUUID().toString() else tokens[0].device_id
        val basic = Credentials.basic(BuildConfig.CLIENT_ID,"")
        val token = redditService.getAccessToken(basic,GRANT_TYPE,deviceId)
        val tokenDB = AccessToken.convert(token)
        if(tokens.isEmpty()) {
            dataBase.tokenDao().insertToken(tokenDB)
        } else {
            tokenDB.id = tokens[0].id
            dataBase.tokenDao().upsertToken(tokenDB)
        }
        return tokenDB
    }

    suspend fun refresh() {
        val token = getToken()
        val auth = "Bearer " + token.access_token
        val dataResponse = service.getTop(auth,T_REDDIT,0,LIMIT)
        val dataPostDB = dataResponse.data.children.map { DataPost.convert(it.data) }
        dataBase.postDao().insert(dataPostDB)
    }

    suspend fun getPost(idPost: String) : DataPost {
        return dataBase.postDao().getPost(idPost)
    }

    suspend fun read(post : DataPost) {
        post.read = true
        dataBase.postDao().upsert(listOf(post))
    }

    suspend fun dismiss(post: DataPost) {
        post.dismiss = true
        dataBase.postDao().upsert(listOf(post))
    }

    suspend fun dismiss(posts: List<DataPost>) {
        posts.forEach{
            it.dismiss = true
        }
        dataBase.postDao().upsert(posts)
    }

}