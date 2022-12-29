package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.ReportRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportApi {
    @POST("/api/users/report")
    fun postReport(@Body request: ReportRequest) : Call<Void>
}