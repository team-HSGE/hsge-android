package com.starters.hsge.domain.usecase

import com.starters.hsge.data.model.remote.request.UserLocationRequest
import com.starters.hsge.domain.repository.EditLocationRepository
import javax.inject.Inject

class PutEditLocationUseCase @Inject constructor(
    private val editLocationRepository: EditLocationRepository
) {
    suspend operator fun invoke(location: UserLocationRequest) =
        editLocationRepository.putLocation(location)
}