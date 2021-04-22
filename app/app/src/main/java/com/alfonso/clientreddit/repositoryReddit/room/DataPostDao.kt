package com.alfonso.clientreddit.repositoryReddit.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alfonso.clientreddit.models.DataPost

@Dao
interface DataPostDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data : List<DataPost>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(data : List<DataPost>)

    @Query("SELECT * FROM DataPost WHERE dismiss = :flag ")
    fun getPost(flag : Boolean) : LiveData<List<DataPost>>

    @Query("SELECT * FROM DataPost WHERE dismiss = :flag ")
    suspend fun getPostSynchronous(flag : Boolean) : List<DataPost>

}