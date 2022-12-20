package com.starters.hsge.domain.repository

interface DeleteDogRepository {
    suspend fun deleteDogProfile(petId: Int)
}