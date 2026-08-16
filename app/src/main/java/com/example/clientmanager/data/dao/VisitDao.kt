package com.example.clientmanager.data.dao

import androidx.room.*
import com.example.clientmanager.data.Visit
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitDao {
    @Query("SELECT * FROM visits WHERE clientId = :clientId ORDER BY visitNumber ASC")
    fun getVisitsForClient(clientId: Long): Flow<List<Visit>>

    @Query("SELECT * FROM visits WHERE clientId = :clientId ORDER BY visitNumber DESC LIMIT 1")
    suspend fun getLatestVisit(clientId: Long): Visit?

    @Query("SELECT MAX(visitNumber) FROM visits WHERE clientId = :clientId")
    suspend fun getMaxVisitNumber(clientId: Long): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: Visit): Long

    @Update
    suspend fun updateVisit(visit: Visit)

    @Delete
    suspend fun deleteVisit(visit: Visit)

    @Query("SELECT * FROM visits ORDER BY date DESC")
    fun getAllVisits(): Flow<List<Visit>>

    // For dashboard-level "all clients" comparison charts
    @Query("""
        SELECT v.* FROM visits v
        INNER JOIN (
            SELECT clientId, MAX(visitNumber) as maxVisit
            FROM visits GROUP BY clientId
        ) latest ON v.clientId = latest.clientId AND v.visitNumber = latest.maxVisit
    """)
    fun getLatestVisitPerClient(): Flow<List<Visit>>
}
