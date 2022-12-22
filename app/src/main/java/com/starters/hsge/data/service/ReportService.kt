package com.starters.hsge.data.service

import com.starters.hsge.data.api.ReportApi
import com.starters.hsge.data.interfaces.ReportInterface
import com.starters.hsge.data.model.remote.request.ReportRequest
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportService(val reportInterface: ReportInterface) {

    fun tryPostReport(reportRequest: ReportRequest){
        val reportApi = RetrofitClient.sRetrofit.create(ReportApi::class.java)

        reportApi.postReport(reportRequest).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                reportInterface.onPostReportSuccess(response.isSuccessful, response.code(), )
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                reportInterface.onPostReportFailure(t.message?: "통신 오류")
            }
        })
    }
}