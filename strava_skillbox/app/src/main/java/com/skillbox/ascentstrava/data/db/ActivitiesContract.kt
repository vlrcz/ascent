package com.skillbox.ascentstrava.data.db

object ActivitiesContract {

    const val TABLE_NAME = "activities"

    object Columns {
        const val ID = "id"
        const val NAME = "name"
        const val TYPE = "type"
        const val STARTED_AT = "start_date_local"
        const val ELAPSED_TIME = "elapsed_time"
        const val DISTANCE = "distance"
        const val DESCRIPTION = "description"
        const val IS_PENDING = "is_pending"
    }
}