package com.alfonso.clientreddit.repositoryReddit.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alfonso.clientreddit.models.AccessToken
import com.alfonso.clientreddit.models.AccessTokenReddit

@Dao
interface AcessTokenDao {
    @Insert
    fun insertToken(tokenReddit : AccessToken)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun upsertToken(tokenReddit: AccessToken)

    @Query("SELECT * FROM AccessToken")
    fun getAll() : LiveData<List<AccessToken>>

    @Query("SELECT * FROM AccessToken")
    fun getAllSynchronous() : List<AccessToken>
}