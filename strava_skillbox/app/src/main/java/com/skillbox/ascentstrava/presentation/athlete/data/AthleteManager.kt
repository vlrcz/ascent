package com.skillbox.ascentstrava.presentation.athlete.data

import com.skillbox.ascentstrava.presentation.athlete.Athlete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AthleteManager @Inject constructor(
    private val athleteRepository: AthleteRepository
) {
    private val athleteListener = MutableStateFlow<Athlete?>(null)

    suspend fun fetchLocalAthlete() {
        flow { emit(athleteRepository.fetchLocalAthlete()) }
            .filterNotNull()
            .catch {
                Timber.e(it)
            }
            .flowOn(Dispatchers.IO)
            .collect {
                athleteListener.value = it
            }
    }

    suspend fun fetchAthlete(): Flow<Unit> {
        return flow { emit(athleteRepository.fetchAthlete()) }
            .map {
                athleteRepository.insertAthlete(it)
                athleteListener.value = it
            }
            .catch {
                Timber.e(it)
            }
            .flowOn(Dispatchers.IO)
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