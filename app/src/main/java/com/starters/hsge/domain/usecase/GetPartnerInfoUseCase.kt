package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.PartnerInfoRepository
import javax.inject.Inject

class GetPartnerInfoUseCase @Inject constructor(
    private var partnerInfoRepository: PartnerInfoRepository
) {
    suspend operator fun invoke(partnerId: Long) = partnerInfoRepository.getPartnerDogs(partnerId)
}