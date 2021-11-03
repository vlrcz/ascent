package com.skillbox.ascentstrava.presentation.athlete.data

import com.skillbox.ascentstrava.presentation.athlete.Athlete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AthleteManager @Inject constructor(
    private val athleteRepository: AthleteRepository
) {
    private val athleteListener = MutableStateFlow<Athlete?>(null)

    suspend fun fetchAthlete() {
        flow { emit(athleteRepository.fetchAthlete()) }
            .catch {
                athleteRepository.fetchLocalAthlete()?.let { emit(it) }
            }
            .flowOn(Dispatchers.IO)
            .collect {
                athleteRepository.insertAthlete(it)
                athleteListener.value = it
            }
    }

    fun observeAthlete() = athleteListener

    fun getProfileUrl(): String? {
        return athleteListener.value?.shareLink
    }

    suspend fun clear() {
        athleteRepository.clearAthlete()
        athleteListener.value = null
    }
}