package com.example.clientmanager.data.dao

import androidx.room.*
import com.example.clientmanager.data.Client
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Query("SELECT * FROM clients ORDER BY name ASC")
    fun getAllClients(): Flow<List<Client>>

    @Query("SELECT * FROM clients WHERE id = :id")
    fun getClientByIdFlow(id: Long): Flow<Client?>

    @Query("SELECT * FROM clients WHERE id = :id")
    suspend fun getClientById(id: Long): Client?

    @Query("SELECT * FROM clients WHERE name LIKE '%' || :query || '%' OR mobileNo LIKE '%' || :query || '%'")
    fun searchClients(query: String): Flow<List<Client>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: Client): Long

    @Update
    suspend fun updateClient(client: Client)

    @Delete
    suspend fun deleteClient(client: Client)

    @Query("SELECT COUNT(*) FROM clients")
    fun getClientCount(): Flow<Int>
}
