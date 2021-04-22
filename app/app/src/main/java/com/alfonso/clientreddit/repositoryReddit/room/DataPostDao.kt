package com.alfonso.clientreddit.repositoryReddit.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alfonso.clientreddit.models.DataPost

@Dao
interface DataPostDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : DataPost)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(data : DataPost)

    @Query("SELECT * FROM DataPost WHERE dismiss = :flag ")
    fun getPost(flag : Boolean) : LiveData<List<DataPost>>

    @Query("SELECT * FROM DataPost WHERE dismiss = :flag ")
    fun getPostSynchronous(flag : Boolean) : List<DataPost>

}