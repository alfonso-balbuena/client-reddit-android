package com.alfonso.clientreddit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alfonso.clientreddit.repositoryReddit.webService.RedditService
import com.alfonso.clientreddit.repositoryReddit.webService.RedditServiceToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class RedditServiceTest {

    private val mockServer = MockWebServer()
    private val service = Utils.getRedditService(mockServer)

    @After
    fun shutdown() {
        mockServer.shutdown()
    }

    @Test
    fun getPostTest() {
        mockServer.enqueue(Utils.getRedditServiceResponse())
        runBlocking {
            val responsePost = service.getTop("","day",0,50)
            assert(responsePost.data.children.count() == 1)
        }
    }
}