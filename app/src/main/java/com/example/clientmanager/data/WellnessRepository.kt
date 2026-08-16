package com.example.clientmanager.data

import kotlinx.coroutines.flow.Flow

class WellnessRepository(private val db: AppDatabase) {

    // --- Client ---
    fun getAllClients(): Flow<List<Client>> = db.clientDao().getAllClients()
    fun getClientByIdFlow(id: Long): Flow<Client?> = db.clientDao().getClientByIdFlow(id)
    suspend fun getClientById(id: Long): Client? = db.clientDao().getClientById(id)
    fun searchClients(q: String): Flow<List<Client>> = db.clientDao().searchClients(q)
    suspend fun insertClient(client: Client): Long = db.clientDao().insertClient(client)
    suspend fun updateClient(client: Client) = db.clientDao().updateClient(client)
    suspend fun deleteClient(client: Client) = db.clientDao().deleteClient(client)
    fun getClientCount(): Flow<Int> = db.clientDao().getClientCount()

    // --- Visits ---
    fun getVisitsForClient(clientId: Long): Flow<List<Visit>> = db.visitDao().getVisitsForClient(clientId)
    suspend fun getMaxVisitNumber(clientId: Long): Int? = db.visitDao().getMaxVisitNumber(clientId)
    suspend fun insertVisit(visit: Visit): Long = db.visitDao().insertVisit(visit)
    suspend fun updateVisit(visit: Visit) = db.visitDao().updateVisit(visit)
    suspend fun deleteVisit(visit: Visit) = db.visitDao().deleteVisit(visit)
    fun getLatestVisitPerClient(): Flow<List<Visit>> = db.visitDao().getLatestVisitPerClient()

    // --- Progress Notes ---
    fun getNotesForClient(clientId: Long): Flow<List<ProgressNote>> = db.progressNoteDao().getNotesForClient(clientId)
    suspend fun insertNote(note: ProgressNote): Long = db.progressNoteDao().insertNote(note)
    suspend fun updateNote(note: ProgressNote) = db.progressNoteDao().updateNote(note)
    suspend fun deleteNote(note: ProgressNote) = db.progressNoteDao().deleteNote(note)
}
