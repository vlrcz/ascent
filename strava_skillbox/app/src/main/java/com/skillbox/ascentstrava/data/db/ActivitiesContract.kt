package com.skillbox.ascentstrava.data.db

object ActivitiesContract {

    const val TABLE_NAME = "activities"

    object Columns {
        const val BD_ID = "bd_id"
        const val STRAVA_ID = "id"
        const val NAME = "name"
        const val TYPE = "type"
        const val STARTED_AT = "start_date_local"
        const val ELAPSED_TIME = "elapsed_time"
        const val DISTANCE = "distance"
        const val DESCRIPTION = "description"
    }
}