package com.skillbox.ascentstrava.presentation.activities.data

import com.skillbox.ascentstrava.network.ConnectionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityAppInitializer @Inject constructor(
    private val pendingActivitiesManager: PendingActivitiesManager,
    private val connectionManager: ConnectionManager
) {
    fun init() {
        GlobalScope.launch(Dispatchers.Default) {
            connectionManager
                .observeNetworkState()
                .filter { it }
                .onEach {
                    pendingActivitiesManager.sendPendingActivities()
                }
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }
}