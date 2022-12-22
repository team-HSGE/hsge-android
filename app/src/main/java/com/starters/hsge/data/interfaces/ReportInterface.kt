package com.starters.hsge.data.interfaces

interface ReportInterface {

    fun onPostReportSuccess(isSuccess: Boolean, code: Int)

    fun onPostReportFailure(message: String)
}