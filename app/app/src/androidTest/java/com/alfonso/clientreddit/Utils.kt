package com.alfonso.clientreddit

import com.alfonso.clientreddit.repositoryReddit.webService.RedditService
import com.alfonso.clientreddit.repositoryReddit.webService.RedditServiceToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class Utils {
    companion object {
        fun getRedditService(mockServer: MockWebServer) : RedditService {
            val client = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .build()
            val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            return Retrofit.Builder()
                .baseUrl(mockServer.url("/"))
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(RedditService::class.java)
        }

        fun getRedditServiceToken(mockServer : MockWebServer) : RedditServiceToken {
            val client = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .build()

            val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

            return Retrofit.Builder()
                .baseUrl(mockServer.url("/"))
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(RedditServiceToken::class.java)
        }

        fun getRedditServiceTokenResponse() : MockResponse {
            val mockResponse = MockResponse()
            mockResponse.setBody("{\n" +
                    "    \"access_token\": \"-wb_qlMgehH7ghFaav3nQ9_-VNvNyAQ\",\n" +
                    "    \"token_type\": \"bearer\",\n" +
                    "    \"device_id\": \"1e73717f-8532-4d69-a5cb-bceb269ca5ab\",\n" +
                    "    \"expires_in\": 3600,\n" +
                    "    \"scope\": \"*\"\n" +
                    "}")
            return mockResponse
        }

        fun getRedditServiceResponse() : MockResponse {
            val response = "{\n" +
                    "    \"kind\": \"Listing\",\n" +
                    "    \"data\": {\n" +
                    "        \"modhash\": \"\",\n" +
                    "        \"dist\": 2,\n" +
                    "        \"children\": [\n" +
                    "            {\n" +
                    "                \"kind\": \"t3\",\n" +
                    "                \"data\": {\n" +
                    "                    \"domain\": \"i.imgur.com\",\n" +
                    "                    \"banned_by\": null,\n" +
                    "                    \"media_embed\": {},\n" +
                    "                    \"subreddit\": \"funny\",\n" +
                    "                    \"selftext_html\": null,\n" +
                    "                    \"selftext\": \"\",\n" +
                    "                    \"likes\": null,\n" +
                    "                    \"user_reports\": [],\n" +
                    "                    \"secure_media\": null,\n" +
                    "                    \"link_flair_text\": null,\n" +
                    "                    \"id\": \"2hqlxp\",\n" +
                    "                    \"gilded\": 0,\n" +
                    "                    \"secure_media_embed\": {},\n" +
                    "                    \"clicked\": false,\n" +
                    "                    \"report_reasons\": null,\n" +
                    "                    \"author\": \"washedupwornout\",\n" +
                    "                    \"media\": null,\n" +
                    "                    \"score\": 4841,\n" +
                    "                    \"approved_by\": null,\n" +
                    "                    \"over_18\": false,\n" +
                    "                    \"hidden\": false,\n" +
                    "                    \"thumbnail\": \"http://b.thumbs.redditmedia.com/9N1f7UGKM5fPZydrsgbb4_SUyyLW7A27um1VOygY3LM.jpg\",\n" +
                    "                    \"subreddit_id\": \"t5_2qh33\",\n" +
                    "                    \"edited\": false,\n" +
                    "                    \"link_flair_css_class\": null,\n" +
                    "                    \"author_flair_css_class\": null,\n" +
                    "                    \"downs\": 0,\n" +
                    "                    \"mod_reports\": [],\n" +
                    "                    \"saved\": false,\n" +
                    "                    \"is_self\": false,\n" +
                    "                    \"name\": \"t3_2hqlxp\",\n" +
                    "                    \"permalink\": \"/r/funny/comments/2hqlxp/man_trying_to_return_a_dogs_toy_gets_tricked_into/\",\n" +
                    "                    \"stickied\": false,\n" +
                    "                    \"created\": 1411975314,\n" +
                    "                    \"url\": \"http://i.imgur.com/4CHXnj2.gif\",\n" +
                    "                    \"author_flair_text\": null,\n" +
                    "                    \"title\": \"Man trying to return a dog's toy gets tricked into playing fetch\",\n" +
                    "                    \"created_utc\": 1411946514,\n" +
                    "                    \"ups\": 4841,\n" +
                    "                    \"num_comments\": 958,\n" +
                    "                    \"visited\": false,\n" +
                    "                    \"num_reports\": null,\n" +
                    "                    \"distinguished\": null\n" +
                    "                }\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "}"
            val mockResponse = MockResponse()
            mockResponse.setBody(response)
            return mockResponse
        }
    }
}