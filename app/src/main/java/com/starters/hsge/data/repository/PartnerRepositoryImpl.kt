package com.starters.hsge.data.repository

import com.starters.hsge.data.api.PartnerApi
import com.starters.hsge.data.model.remote.response.UserDogResponse
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.repository.PartnerRepository
import javax.inject.Inject

class PartnerRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: PartnerApi
): PartnerRepository {
    override suspend fun getPartnerDogs(partnerId: Long): List<UserDogResponse> {
        return api.getPartnerDogs(partnerId)
    }
}