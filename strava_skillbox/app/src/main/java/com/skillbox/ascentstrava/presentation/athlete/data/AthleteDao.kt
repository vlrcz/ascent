package com.skillbox.ascentstrava.presentation.athlete.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skillbox.ascentstrava.presentation.athlete.Athlete

@Dao
interface AthleteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(athlete: Athlete)

    @Query("SELECT * FROM ${AthleteContract.TABLE_NAME}")
    suspend fun fetchAthlete(): Athlete?

    @Query("DELETE FROM ${AthleteContract.TABLE_NAME}")
    suspend fun clearAthlete()
        
}