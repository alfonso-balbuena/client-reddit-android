package com.alfonso.clientreddit.repositoryReddit.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alfonso.clientreddit.models.AccessToken
import com.alfonso.clientreddit.models.DataPost


@Database(entities = [AccessToken::class, DataPost::class],version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun tokenDao() : AcessTokenDao
    abstract fun postDao() : DataPostDao
}