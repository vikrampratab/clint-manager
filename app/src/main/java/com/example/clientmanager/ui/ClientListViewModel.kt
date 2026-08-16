package com.example.clientmanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientmanager.data.Client
import com.example.clientmanager.data.Visit
import com.example.clientmanager.data.WellnessRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ClientListViewModel(private val repo: WellnessRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    fun onSearchChange(q: String) { searchQuery.value = q }

    val clients: StateFlow<List<Client>> = searchQuery
        .flatMapLatest { q -> if (q.isBlank()) repo.getAllClients() else repo.searchClients(q) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val clientCount: StateFlow<Int> = repo.getClientCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Latest visit per client -> used for dashboard-wide comparison charts
    val latestVisits: StateFlow<List<Visit>> = repo.getLatestVisitPerClient()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteClient(client: Client) {
        viewModelScope.launch { repo.deleteClient(client) }
    }
}
