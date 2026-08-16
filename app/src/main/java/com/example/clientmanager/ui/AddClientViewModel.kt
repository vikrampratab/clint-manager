package com.example.clientmanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientmanager.data.Client
import com.example.clientmanager.data.WellnessRepository
import kotlinx.coroutines.launch

class AddClientViewModel(private val repo: WellnessRepository) : ViewModel() {
    fun addClient(client: Client, onSaved: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repo.insertClient(client)
            onSaved(id)
        }
    }
}
