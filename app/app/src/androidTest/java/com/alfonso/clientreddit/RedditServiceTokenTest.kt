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

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()


    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val service = Retrofit.Builder()
        .baseUrl(mockServer.url("/"))
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(RedditServiceToken::class.java)

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
        val mockResponse = MockResponse()
        mockResponse.setBody("{\n" +
                "    \"access_token\": \"-wb_qlMgehH7ghFaav3nQ9_-VNvNyAQ\",\n" +
                "    \"token_type\": \"bearer\",\n" +
                "    \"device_id\": \"1e73717f-8532-4d69-a5cb-bceb269ca5ab\",\n" +
                "    \"expires_in\": 3600,\n" +
                "    \"scope\": \"*\"\n" +
                "}")
        mockServer.enqueue(mockResponse)
        runBlocking {
            val token = service.getAccessToken("","https://oauth.reddit.com/grants/installed_client","")
            assert(token.token_type == "bearer")
        }
    }
}