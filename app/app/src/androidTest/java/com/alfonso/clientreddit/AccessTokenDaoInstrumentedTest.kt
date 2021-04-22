package com.alfonso.clientreddit

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alfonso.clientreddit.models.AccessToken
import com.alfonso.clientreddit.models.AccessTokenReddit
import com.alfonso.clientreddit.repositoryReddit.room.AcessTokenDao
import com.alfonso.clientreddit.repositoryReddit.room.AppDataBase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AccessTokenDaoInstrumentedTest {

    private lateinit var accessTokenDao: AcessTokenDao
    private lateinit var db : AppDataBase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context,AppDataBase::class.java).build()
        accessTokenDao = db.tokenDao()
    }

    @After
    @Throws(IOException::class)
    fun finish() {
        db.close()
    }

    @Test
    fun insertTest() {
        val token = AccessToken(access_token = "-3d4oxkIEeqzGOzCCG1KsOuWbkjhSPw",token_type = "bearer",device_id = "1e73717f-8532-4d69-a5cb-bceb269ca5ab",expires_in = 3600,scope = "*",id = 0)
        runBlocking {
            accessTokenDao.insertToken(token)
            val list = accessTokenDao.getAllSynchronous()
            assert(list[0].device_id == "1e73717f-8532-4d69-a5cb-bceb269ca5ab")
        }
    }

    @Test
    fun upsertTest() {
        val token = AccessToken(access_token = "",token_type = "",device_id = "1e73717f-8532-4d69-a5cb-bceb269ca5ab",expires_in = 0,scope = "",id = -1)
        runBlocking {
            accessTokenDao.insertToken(token)
            val newToken = AccessToken(access_token = "-3d4oxkIEeqzGOzCCG1KsOuWbkjhSPw",token_type = "bearer",device_id = "1e73717f-8532-4d69-a5cb-bceb269ca5ab",expires_in = 3600,scope = "*",id = token.id)
            accessTokenDao.upsertToken(newToken)
            val list = accessTokenDao.getAllSynchronous()
            assert(list[0].access_token == "-3d4oxkIEeqzGOzCCG1KsOuWbkjhSPw")
        }
    }

}