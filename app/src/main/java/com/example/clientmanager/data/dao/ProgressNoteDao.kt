package com.example.clientmanager.data.dao

import androidx.room.*
import com.example.clientmanager.data.ProgressNote
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressNoteDao {
    @Query("SELECT * FROM progress_notes WHERE clientId = :clientId ORDER BY date DESC")
    fun getNotesForClient(clientId: Long): Flow<List<ProgressNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: ProgressNote): Long

    @Update
    suspend fun updateNote(note: ProgressNote)

    @Delete
    suspend fun deleteNote(note: ProgressNote)
}
