package com.alfonso.clientreddit

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alfonso.clientreddit.repositoryReddit.webService.RedditModule
import com.alfonso.clientreddit.repositoryReddit.webService.RedditServiceToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.runner.RunWith
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class RedditServiceTokenTest {

    private val mockServer = MockWebServer()

    private val service = Utils.getRedditServiceToken(mockServer)

    @Before
    fun start() {
        //mockServer.start()
    }

    @After
    fun shutdown() {
        mockServer.shutdown()
    }

    @Test
    fun getTokenTest() {
        mockServer.enqueue(Utils.getRedditServiceTokenResponse())
        runBlocking {
            val token = service.getAccessToken("","https://oauth.reddit.com/grants/installed_client","")
            assert(token.token_type == "bearer")
        }
    }
}