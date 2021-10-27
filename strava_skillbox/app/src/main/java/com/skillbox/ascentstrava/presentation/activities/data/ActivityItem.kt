package com.skillbox.ascentstrava.presentation.activities.data

data class ActivityItem(
    val uniqueId: String? = null,
    val stravaId: Long?,
    val name: String?,
    val type: String?,
    val startedAt: String?,
    val elapsedTime: Int?,
    val distance: Float?,
    val description: String?,
    val isPending: Boolean? = false
)