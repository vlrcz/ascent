package com.skillbox.ascentstrava.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skillbox.ascentstrava.presentation.activities.data.ActivityModel

@Dao
interface ActivitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityModel(activityModel: ActivityModel)

    @Query("SELECT * FROM ${ActivitiesContract.TABLE_NAME}")
    suspend fun getActivities(): List<ActivityModel>

}