package com.example.clientmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clientmanager.ui.ClientListViewModel
import com.example.clientmanager.ui.RepoViewModelFactory
import com.example.clientmanager.ui.components.ClientComparisonBarChart
import com.example.clientmanager.ui.components.GoalDistributionPieChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onBack: () -> Unit) {
    val vm: ClientListViewModel = viewModel(factory = RepoViewModelFactory { ClientListViewModel(it) })
    val clients by vm.clients.collectAsState()
    val latestVisits by vm.latestVisits.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Overview Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Total Clients", clients.size.toString(), Modifier.weight(1f))
                StatCard(
                    "Avg Weight",
                    latestVisits.mapNotNull { it.weightKg }.let { l -> if (l.isEmpty()) "-" else "%.1f kg".format(l.average()) },
                    Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(20.dp))
            if (clients.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(vertical = 40.dp), contentAlignment = Alignment.Center) {
                    Text("Add clients to see analytics here.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                return@Column
            }

            SectionTitle("Goal Distribution")
            GoalDistributionPieChart(clients = clients)

            Spacer(Modifier.height(24.dp))
            SectionTitle("Latest Weight by Client")
            ClientComparisonBarChart(clients = clients, latestVisits = latestVisits)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 10.dp))
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(Modifier.padding(16.dp)) {
            Text(value, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
