package com.alfonso.clientreddit.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class AccessTokenReddit(val access_token: String,
                             val token_type: String,
                             val device_id : String,
                             val expires_in : Int,
                             val scope : String)

@Entity
data class AccessToken(@PrimaryKey(autoGenerate = true) var id : Int,
                       @ColumnInfo(name = "token") val access_token: String,
                       @ColumnInfo(name = "type") val token_type: String,
                       @ColumnInfo(name = "deviceId") val device_id : String,
                       @ColumnInfo(name= "expires") val expires_in : Int,
                       @ColumnInfo(name = "scope") val scope : String) {

    companion object {
        fun convert(data : AccessTokenReddit) : AccessToken {
            return AccessToken(-1,data.access_token,data.token_type,data.device_id,data.expires_in,data.scope)
        }
    }
}