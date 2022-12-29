package com.starters.hsge.data.repository

import com.starters.hsge.data.api.PartnerApi
import com.starters.hsge.data.model.remote.response.MyDogResponse
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.repository.PartnerInfoRepository
import javax.inject.Inject

class PartnerInfoRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: PartnerApi
): PartnerInfoRepository {
    override suspend fun getPartnerDogs(partnerId: Long): List<MyDogResponse> {
        return api.getPartnerDogs(partnerId)
    }
}