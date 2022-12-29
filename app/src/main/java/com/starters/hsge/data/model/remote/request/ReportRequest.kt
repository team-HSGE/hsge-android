package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportRequest(
    @SerialName("description") val description : String,
    @SerialName("reportee") val reportee : Long
)