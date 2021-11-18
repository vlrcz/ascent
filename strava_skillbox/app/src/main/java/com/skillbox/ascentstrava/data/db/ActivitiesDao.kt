package com.skillbox.ascentstrava.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityToDb(activityEntity: ActivityEntity)

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfActivityToDb(list: List<ActivityEntity>)

    @Query("SELECT * FROM ${ActivitiesContract.TABLE_NAME} LIMIT :items OFFSET :page ")
    suspend fun getActivities(page: Int, items: Int): List<ActivityEntity>

    @Query("UPDATE ${ActivitiesContract.TABLE_NAME} SET id = :stravaId, is_pending = :isPending  WHERE ${ActivitiesContract.Columns.ID} = :uniqueId")
    suspend fun updateEntityByModel(stravaId: String, uniqueId: String, isPending: Boolean)

    @Query("DELETE FROM ${ActivitiesContract.TABLE_NAME} WHERE ${ActivitiesContract.Columns.ID} = :uniqueId")
    suspend fun deleteEntityByUniqueId(uniqueId: String)

    @Query("SELECT * FROM ${ActivitiesContract.TABLE_NAME} WHERE ${ActivitiesContract.Columns.IS_PENDING} = :isPending")
    suspend fun getListOfPendingActivities(isPending: Boolean): List<ActivityEntity>
}