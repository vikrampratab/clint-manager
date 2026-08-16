package com.example.clientmanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientmanager.data.Client
import com.example.clientmanager.data.ProgressNote
import com.example.clientmanager.data.Visit
import com.example.clientmanager.data.WellnessRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClientDetailViewModel(
    private val repo: WellnessRepository,
    private val clientId: Long
) : ViewModel() {

    val client: StateFlow<Client?> = repo.getClientByIdFlow(clientId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val visits: StateFlow<List<Visit>> = repo.getVisitsForClient(clientId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notes: StateFlow<List<ProgressNote>> = repo.getNotesForClient(clientId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveClient(client: Client, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repo.updateClient(client)
            onDone()
        }
    }

    fun addVisit(visit: Visit, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val nextNum = (repo.getMaxVisitNumber(clientId) ?: 0) + 1
            repo.insertVisit(visit.copy(clientId = clientId, visitNumber = nextNum))
            onDone()
        }
    }

    fun updateVisit(visit: Visit) {
        viewModelScope.launch { repo.updateVisit(visit) }
    }

    fun deleteVisit(visit: Visit) {
        viewModelScope.launch { repo.deleteVisit(visit) }
    }

    fun addNote(note: ProgressNote, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repo.insertNote(note.copy(clientId = clientId))
            onDone()
        }
    }

    fun deleteNote(note: ProgressNote) {
        viewModelScope.launch { repo.deleteNote(note) }
    }
}
