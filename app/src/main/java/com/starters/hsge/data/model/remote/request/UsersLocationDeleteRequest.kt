package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UsersLocationDeleteRequest(
    @SerialName("name") val name: String
)
