package com.skillbox.ascentstrava.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = ActivitiesContract.TABLE_NAME
)
data class ActivityEntity(
    @PrimaryKey
    @ColumnInfo(name = ActivitiesContract.Columns.ID)
    val id: String,
    @ColumnInfo(name = ActivitiesContract.Columns.NAME)
    val name: String?,
    @ColumnInfo(name = ActivitiesContract.Columns.TYPE)
    val type: String?,
    @ColumnInfo(name = ActivitiesContract.Columns.STARTED_AT)
    val startedAt: Long?,
    @ColumnInfo(name = ActivitiesContract.Columns.ELAPSED_TIME)
    val elapsedTime: Int?,
    @ColumnInfo(name = ActivitiesContract.Columns.DISTANCE)
    val distance: Float?,
    @ColumnInfo(name = ActivitiesContract.Columns.DESCRIPTION)
    val description: String?,
    @ColumnInfo(name = ActivitiesContract.Columns.IS_PENDING)
    val isPending: Boolean
)
