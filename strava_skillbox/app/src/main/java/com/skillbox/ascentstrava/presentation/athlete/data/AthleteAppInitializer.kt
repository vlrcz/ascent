package com.skillbox.ascentstrava.presentation.athlete.data

import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.network.ConnectionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AthleteAppInitializer @Inject constructor(
    private val authManager: AuthManager,
    private val athleteManager: AthleteManager,
    private val connectionManager: ConnectionManager
) {
    fun init() {
        GlobalScope.launch(Dispatchers.Default) {
            authManager
                .observeAuth()
                .onSubscription {
                    athleteManager.fetchLocalAthlete()
                }
                .combine(connectionManager.observeNetworkState()) { isLoggedIn, _ ->
                    isLoggedIn
                }
                .flatMapConcat { isLoggedIn ->
                    if (isLoggedIn) {
                        athleteManager.fetchAthlete()
                    } else {
                        flowOf(athleteManager.clear())
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }
}