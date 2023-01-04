package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.response.UserDogResponse

interface PartnerRepository {
    suspend fun getPartnerDogs(partnerId: Long): List<UserDogResponse>
}