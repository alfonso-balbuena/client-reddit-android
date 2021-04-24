package com.alfonso.clientreddit

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alfonso.clientreddit.repositoryReddit.repository.PostRepository
import com.alfonso.clientreddit.repositoryReddit.room.AppDataBase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    private lateinit var db : AppDataBase
    private val mockServer = MockWebServer()
    private val serviceToken = Utils.getRedditServiceToken(mockServer)
    private val service = Utils.getRedditService(mockServer)
    private lateinit var repository : PostRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
        repository = PostRepository(serviceToken,service,db)
        mockServer.enqueue(Utils.getRedditServiceTokenResponse())
        mockServer.enqueue(Utils.getRedditServiceResponse())
        mockServer.enqueue(Utils.getRedditServiceTokenResponse())
        mockServer.enqueue(Utils.getRedditServiceResponse2())
    }

    @After
    @Throws(IOException::class)
    fun finish() {
        db.close()
        mockServer.shutdown()
    }

    @Test
    fun getToken() {

        runBlocking {
            val token = repository.getToken()
            assert(token.access_token == "-wb_qlMgehH7ghFaav3nQ9_-VNvNyAQ")
            val tokenList = db.tokenDao().getAllSynchronous()
            assert(tokenList.isNotEmpty())
            assert(tokenList[0].access_token == "-wb_qlMgehH7ghFaav3nQ9_-VNvNyAQ")
        }
    }

    @Test
    fun initTest() {
        runBlocking {
            repository.init()
            val tokenList = db.tokenDao().getAllSynchronous()
            assert(tokenList.isNotEmpty())
            val posts = db.postDao().getPostsSuspend(false)
            assert(posts.isNotEmpty())
        }
    }

    @Test
    fun getPostTest() {
        runBlocking {
            repository.init()
            val post = repository.getPost("2hqlxp")
            assert(post.id == "2hqlxp")
            assert(post.author == "washedupwornout")
        }
    }

    @Test
    fun readPostTest() {
        runBlocking {
            repository.init()
            var post = repository.getPost("2hqlxp")
            assert(!post.read)
            post.read = true
            repository.read(post)
            post = repository.getPost("2hqlxp")
            assert(post.read)
        }
    }

    @Test
    fun dismissTest() {
        runBlocking {
            repository.init()
            val post = repository.getPost("2hqlxp")
            repository.dismiss(post)
            val postList = db.postDao().getPostsSuspend(false)
            assert(postList.isEmpty())
        }
    }

    @Test
    fun refreshTest() {
        runBlocking {
            repository.init()
            var post = repository.getPost("2hqlxp")
            assert(!post.read)
            post.read = true
            repository.read(post)
            repository.refresh()
            val postList = db.postDao().getPostsSuspend(false)
            assert(postList.size == 2)
            post = repository.getPost("2hqlxp")
            assert(post.read)
        }
    }

    @Test
    fun paginationTest() {
        runBlocking {
            repository.init()
            delay(1000)
            assert(repository.posts.value?.size == 1)
            repository.hasNext.value?.let { assert(!it) }
            repository.hasPrevious.value?.let { assert(!it) }
            repository.refresh()
            delay(1000)
            val oldPost = repository.posts.value?.get(0)
            assert(repository.posts.value?.size == 1)
            repository.hasNext.value?.let { assert(it) }
            repository.hasPrevious.value?.let { assert(!it) }
            repository.next()
            delay(1000)
            repository.hasNext.value?.let { assert(!it) }
            repository.hasPrevious.value?.let { assert(it) }
            repository.previous()
            delay(1000)
            assert(oldPost == repository.posts.value?.get(0))
            repository.hasNext.value?.let { assert(it) }
            repository.hasPrevious.value?.let { assert(!it) }
        }
    }
}