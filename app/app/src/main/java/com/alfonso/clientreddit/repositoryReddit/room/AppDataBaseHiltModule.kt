package com.alfonso.clientreddit.repositoryReddit.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppDataBaseHiltModule {

    @Singleton
    @Provides
    fun getAppDataBase(@ApplicationContext context: Context) : AppDataBase {
        val name = "DataBasePost"
        return Room.databaseBuilder(context,AppDataBase::class.java,name).build()
    }

}