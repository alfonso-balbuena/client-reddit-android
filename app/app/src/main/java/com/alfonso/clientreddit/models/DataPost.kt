package com.alfonso.clientreddit.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataPost(@PrimaryKey val id : String,
                    val  title : String,
                    val numComments : Int,
                    val author : String,
                    val thumbnail : String,
                    val createdUtc : Long,
                    val link : String,
                    var dismiss : Boolean,
                    var read : Boolean) {
    companion object {
        fun convert(serviceData : RedditListingChildData) : DataPost {
            return DataPost(serviceData.id,serviceData.title,serviceData.num_comments,serviceData.author,serviceData.thumbnail,serviceData.created_utc,serviceData.permalink,
                dismiss = false,
                read = false
            )
        }
    }
}