package com.skillbox.ascentstrava.presentation.activities.data

import java.text.SimpleDateFormat
import java.util.*

data class ActivityItem(
    val uniqueId: String? = null,
    val stravaId: Long?,
    val athleteName: String,
    val athleteImage: String,
    val name: String?,
    val type: String?,
    val startedAt: String?,
    val elapsedTime: String?,
    val distance: String?,
    val description: String?,
    val isPending: Boolean = false
) {

    companion object {
        val currentFormat = SimpleDateFormat(
            "MMM dd,yyyy hh:mm a",
            Locale.ROOT
        )
    }

    val date = stringToDate(startedAt)

    private fun stringToDate(date: String?): Date? {
        if (date == null) return null
        return currentFormat.parse(date)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ActivityItem

        if (stravaId != other.stravaId) return false
        if (athleteName != other.athleteName) return false
        if (athleteImage != other.athleteImage) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (startedAt != other.startedAt) return false
        if (elapsedTime != other.elapsedTime) return false
        if (distance != other.distance) return false
        if (description != other.description) return false
        if (isPending != other.isPending) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stravaId?.hashCode() ?: 0
        result = 31 * result + athleteName.hashCode()
        result = 31 * result + athleteImage.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (startedAt?.hashCode() ?: 0)
        result = 31 * result + (elapsedTime?.hashCode() ?: 0)
        result = 31 * result + (distance?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + isPending.hashCode()
        result = 31 * result + (date?.hashCode() ?: 0)
        return result
    }
}