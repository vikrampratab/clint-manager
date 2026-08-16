package com.example.clientmanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.clientmanager.ClientManagerApp
import com.example.clientmanager.data.WellnessRepository

/**
 * Generic factory: builds any ViewModel that takes a WellnessRepository as its
 * single constructor argument, pulling the repository from the Application.
 */
class RepoViewModelFactory<T : ViewModel>(
    private val creator: (WellnessRepository) -> T
) : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>, extras: CreationExtras): VM {
        val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ClientManagerApp
        val repo = WellnessRepository(app.database)
        @Suppress("UNCHECKED_CAST")
        return creator(repo) as VM
    }
}
