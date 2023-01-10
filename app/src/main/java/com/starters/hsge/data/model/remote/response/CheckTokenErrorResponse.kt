package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CheckTokenErrorResponse(
    @SerialName("message") var message: String? = "",
    @SerialName("time") var time : String? = ""
)
