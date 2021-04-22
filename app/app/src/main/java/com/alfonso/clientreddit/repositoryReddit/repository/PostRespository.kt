package com.alfonso.clientreddit.repositoryReddit.repository

import androidx.lifecycle.LiveData
import com.alfonso.clientreddit.models.DataPost
import com.alfonso.clientreddit.repositoryReddit.room.AppDataBase
import com.alfonso.clientreddit.repositoryReddit.webService.RedditService
import com.alfonso.clientreddit.repositoryReddit.webService.RedditServiceToken
import kotlinx.coroutines.*
import javax.inject.Inject

class PostRespository @Inject constructor(private val redditService : RedditServiceToken,private val service : RedditService, private val dataBase : AppDataBase) {

    val posts : LiveData<List<DataPost>> = dataBase.postDao().getPost(false)
    private val repositoryJob = SupervisorJob()
    private val repositoryScope = CoroutineScope(repositoryJob + Dispatchers.Main)

    init {
        repositoryScope.launch {
            val list = dataBase.postDao().getPostSynchronous(false)
            if (list.isEmpty()) {
                val newToken = getToken()
                val newPosts = service.getTop("","month",0,50)
                val dataBasePost = newPosts.data.children.map { DataPost.convert(it.data) }
                dataBase.postDao().insert(dataBasePost)
            }
        }
    }

    suspend fun getToken() {
        withContext(Dispatchers.IO) {

        }
    }

    suspend fun refresh() {
        withContext(Dispatchers.IO) {

        }
    }

    suspend fun read(post: DataPost) {
        withContext(Dispatchers.IO) {

        }
    }

    suspend fun dismiss(post: DataPost) {
        withContext(Dispatchers.IO) {

        }
    }

    suspend fun dismiss(posts: List<DataPost>) {
        withContext(Dispatchers.IO) {

        }
    }

}