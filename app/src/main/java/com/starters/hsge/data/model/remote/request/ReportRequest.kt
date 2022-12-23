package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class ReportRequest(
    val description : String,
    val reportee : Int
)