package com.alfonso.clientreddit.models

data class AcessToken(val access_token: String, val token_type: String, val device_id : String, val expires_in : Int, val scope : String)
