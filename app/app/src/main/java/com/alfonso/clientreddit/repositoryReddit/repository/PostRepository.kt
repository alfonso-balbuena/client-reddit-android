package com.alfonso.clientreddit.repositoryReddit.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alfonso.clientreddit.BuildConfig
import com.alfonso.clientreddit.models.AccessToken
import com.alfonso.clientreddit.models.DataPost
import com.alfonso.clientreddit.repositoryReddit.room.AppDataBase
import com.alfonso.clientreddit.repositoryReddit.webService.RedditService
import com.alfonso.clientreddit.repositoryReddit.webService.RedditServiceToken

import okhttp3.Credentials
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class PostRepository @Inject constructor(private val redditService : RedditServiceToken, private val service : RedditService, private val dataBase : AppDataBase) {

    private val GRANT_TYPE = "https://oauth.reddit.com/grants/installed_client"
    private val T_REDDIT = "month"
    private val LIMIT = 15
    private val ELEMENTS_PAG = 5

    private val _isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val isLoading : LiveData<Boolean>
    get() = _isLoading

    private var startIndex = 0
    private var endIndex = ELEMENTS_PAG

    private lateinit var _allPosts : List<DataPost>
    private val _posts : MutableLiveData<List<DataPost>> = MutableLiveData()
    val posts : LiveData<List<DataPost>>
    get() = _posts

    private var _hasNext : MutableLiveData<Boolean>  = MutableLiveData()
    private var _hasPrevious : MutableLiveData<Boolean> = MutableLiveData()
    val hasNext : LiveData<Boolean>
    get() = _hasNext
    val hasPrevious : LiveData<Boolean>
    get() = _hasPrevious

    init {
        _isLoading.postValue( false)
    }

    suspend fun init() {
        _allPosts = dataBase.postDao().getPostsSuspend(false)
        if (_allPosts.isEmpty()) {
            refresh()
        }
        getNewPag()
    }

    private fun getNewPag() {
        val auxEndIndex = if (endIndex > _allPosts.size) _allPosts.size else endIndex
        _posts.postValue(_allPosts.subList(startIndex,auxEndIndex))
        _posts.value?.forEach { Timber.d(it.toString()) }
        updateFlagsForPagination()
    }

    private fun updateFlagsForPagination() {
        _hasPrevious.postValue(startIndex > 0)
        _hasNext.postValue(_allPosts.size > endIndex)
    }

    fun next() {
        startIndex += ELEMENTS_PAG
        endIndex += ELEMENTS_PAG
        getNewPag()
    }

    fun previous() {
        startIndex -= ELEMENTS_PAG
        endIndex -= ELEMENTS_PAG
        getNewPag()
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
        Timber.d(tokenDB.toString())
        return tokenDB
    }

    suspend fun refresh() {
        _isLoading.postValue(true)
        val token = getToken()
        val auth = "Bearer " + token.access_token
        val dataResponse = service.getTop(auth,T_REDDIT,0,LIMIT)
        val dataPostDB = dataResponse.data.children.map { DataPost.convert(it.data) }
        Timber.d("Getting ${dataPostDB.size} items..." )
        dataPostDB.forEach{
            Timber.d(it.title)
        }
        dataBase.postDao().insert(dataPostDB)
        restart()
        _isLoading.postValue(false)
    }

    private suspend fun getChangesInTable() {
        _allPosts = dataBase.postDao().getPostsSuspend(false)
        getNewPag()
    }

    private suspend fun restart() {
        startIndex = 0
        endIndex = ELEMENTS_PAG
        getChangesInTable()
    }

    suspend fun getPost(idPost: String) : DataPost {
        return dataBase.postDao().getPost(idPost)
    }

    suspend fun read(post : DataPost) {
        val newPost = DataPost(post.id,post.title,post.numComments,post.author,post.thumbnail,post.createdUtc,post.link,post.dismiss,true)
        dataBase.postDao().upsert(listOf(newPost))
        getChangesInTable()
    }

    suspend fun dismiss(post: DataPost) {
        post.dismiss = true
        dataBase.postDao().upsert(listOf(post))
        getChangesInTable()
    }

    suspend fun dismiss(posts: List<DataPost>) {
        posts.forEach{
            it.dismiss = true
        }
        dataBase.postDao().upsert(posts)
        getChangesInTable()
    }

}