package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.PartnerRepository
import javax.inject.Inject

class GetPartnerUseCase @Inject constructor(
    private var partnerRepository: PartnerRepository
) {
    suspend operator fun invoke(partnerId: Long) = partnerRepository.getPartnerDogs(partnerId)
}