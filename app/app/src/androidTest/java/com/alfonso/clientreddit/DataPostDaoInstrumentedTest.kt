package com.alfonso.clientreddit

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alfonso.clientreddit.models.DataPost
import com.alfonso.clientreddit.repositoryReddit.room.AppDataBase
import com.alfonso.clientreddit.repositoryReddit.room.DataPostDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
//import kotlinx.coroutines.test.setMain


@RunWith(AndroidJUnit4::class)
class DataPostDaoInstrumentedTest {

    private lateinit var dataPostDao: DataPostDao
    private lateinit var db : AppDataBase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
        dataPostDao = db.postDao()
    }

    @After
    @Throws(IOException::class)
    fun finish() {
        db.close()
    }

    @Test
    fun insertTest() {
        val postData = DataPost("aaaaaaaa","Title",5,"Me","",1619053076,"",false, read = false)
        runBlocking {
            dataPostDao.insert(listOf(postData))
            val list = dataPostDao.getPostsSuspend(false)
            assert(list[0].id == "aaaaaaaa")
        }

    }

    @Test
    fun insertReplaceDuplicateTest() {
        val postData = DataPost("aaaaaaaa","Title",5,"Me","",1619053076,"",false, read = false)
        runBlocking {
            dataPostDao.insert(listOf(postData))
            postData.dismiss = true
            dataPostDao.insert(listOf(postData))
            val list = dataPostDao.getPostsSuspend(false)
            assert(list.isEmpty())
        }

    }

    @Test
    fun upsertTest() {
        val postData = DataPost("aaaaaaaa","Title",5,"Me","",1619053076,"",false, read = false)
        runBlocking {
            dataPostDao.insert(listOf(postData))
            postData.dismiss = true
            dataPostDao.upsert(listOf(postData))
            val list = dataPostDao.getPostsSuspend(false)
            assert(list.isEmpty())
        }
    }

    @Test
    fun getPostDataTest() {
        val postData = DataPost("aaaaaaaa","Title",5,"Me","",1619053076,"",false, read = false)
        runBlocking {
            dataPostDao.insert(listOf(postData))
            val dataPostDB = dataPostDao.getPost(postData.id)
            assert(dataPostDB == postData)
        }
    }
}