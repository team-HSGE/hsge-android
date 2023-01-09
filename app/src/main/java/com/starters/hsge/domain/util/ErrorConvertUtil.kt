package com.starters.hsge.domain.util

import com.starters.hsge.data.model.remote.response.CheckTokenErrorResponse
import com.starters.hsge.network.RetrofitClient
import okhttp3.ResponseBody

object ErrorConvertUtil {
    fun getErrorResponse(errorBody: ResponseBody): CheckTokenErrorResponse {
        return RetrofitClient.sRetrofit.responseBodyConverter<CheckTokenErrorResponse>(CheckTokenErrorResponse::class.java, CheckTokenErrorResponse::class.java.annotations).convert(errorBody)!!
    }
}
