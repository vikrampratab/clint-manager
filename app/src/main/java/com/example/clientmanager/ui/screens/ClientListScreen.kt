package com.example.clientmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clientmanager.data.Client
import com.example.clientmanager.ui.ClientListViewModel
import com.example.clientmanager.ui.RepoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen(
    onAddClient: () -> Unit,
    onOpenClient: (Long) -> Unit,
    onOpenDashboard: () -> Unit
) {
    val vm: ClientListViewModel = viewModel(factory = RepoViewModelFactory { ClientListViewModel(it) })
    val clients by vm.clients.collectAsState()
    val count by vm.clientCount.collectAsState()
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gaurav Wellness Centre", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onOpenDashboard) {
                        Icon(Icons.Default.BarChart, contentDescription = "Dashboard")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClient) {
                Icon(Icons.Default.Add, contentDescription = "Add Client")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatChip(label = "Total Clients", value = count.toString())
            }

            OutlinedTextField(
                value = query,
                onValueChange = { query = it; vm.onSearchChange(it) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                placeholder = { Text("Search by name or mobile...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            if (clients.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No clients yet. Tap + to add your first client.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(clients, key = { it.id }) { client ->
                        ClientCard(client = client, onClick = { onOpenClient(client.id) })
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: String) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Text(value, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ClientCard(client: Client, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(client.name, style = MaterialTheme.typography.titleMedium)
                Text(client.mobileNo, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (client.goal.isNotBlank()) {
                    Text("Goal: ${client.goal}", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
