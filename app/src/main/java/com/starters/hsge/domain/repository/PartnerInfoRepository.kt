package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.response.MyDogResponse

interface PartnerInfoRepository {
    suspend fun getPartnerDogs(partnerId: Long): List<MyDogResponse>
}