package com.alfonso.clientreddit.repositoryReddit.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alfonso.clientreddit.models.DataPost

@Dao
interface DataPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data : List<DataPost>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(data : List<DataPost>)

    @Query("SELECT * FROM DataPost WHERE id = :id")
    suspend fun getPost(id : String) : DataPost

    @Query("SELECT * FROM DataPost WHERE dismiss = :flag ")
    fun getPosts(flag : Boolean) : LiveData<List<DataPost>>

    @Query("SELECT * FROM DataPost WHERE dismiss = :flag ")
    suspend fun getPostsSuspend(flag : Boolean) : List<DataPost>

}