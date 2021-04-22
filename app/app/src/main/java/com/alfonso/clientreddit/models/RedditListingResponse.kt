package com.alfonso.clientreddit.models

data class RedditListingChildData(val id: String, val  title : String, val num_comments : Int,val author : String, val thumbnail : String, val created_utc : Long)

data class RedditListingChild(val kind : String,val data : RedditListingChildData)

data class RedditListingData(val modhash : String,val dist : Int,val children : List<RedditListingChild>)

data class RedditListingResponse(val data : RedditListingData)
