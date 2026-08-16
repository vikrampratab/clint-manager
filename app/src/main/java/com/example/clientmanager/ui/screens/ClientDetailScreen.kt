package com.example.clientmanager.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clientmanager.data.Visit
import com.example.clientmanager.ui.ClientDetailViewModel
import com.example.clientmanager.ui.RepoViewModelFactory
import com.example.clientmanager.ui.components.WeightTrendChart
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailScreen(
    clientId: Long,
    onBack: () -> Unit,
    onAddVisit: () -> Unit,
    onExportPdf: () -> Unit
) {
    val vm: ClientDetailViewModel = viewModel(
        factory = RepoViewModelFactory { ClientDetailViewModel(it, clientId) }
    )
    val client by vm.client.collectAsState()
    val visits by vm.visits.collectAsState()

    var tab by remember { mutableStateOf(0) }
    val tabs = listOf("Info", "Visits", "Chart")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(client?.name ?: "Client") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onExportPdf) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF")
                    }
                }
            )
        },
        floatingActionButton = {
            if (tab == 1) {
                ExtendedFloatingActionButton(onClick = onAddVisit, icon = { Icon(Icons.Default.Add, null) }, text = { Text("Add Visit") })
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            TabRow(selectedTabIndex = tab) {
                tabs.forEachIndexed { i, t ->
                    Tab(selected = tab == i, onClick = { tab = i }, text = { Text(t) })
                }
            }

            when (tab) {
                0 -> client?.let { InfoTab(it) }
                1 -> VisitsTab(visits, onDelete = { vm.deleteVisit(it) })
                2 -> ChartTab(visits)
            }
        }
    }
}

@Composable
private fun InfoTab(client: com.example.clientmanager.data.Client) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        InfoSection("Personal Information") {
            InfoRow("Mobile", client.mobileNo)
            InfoRow("DOB", client.dob)
            InfoRow("Age", client.age?.toString() ?: "-")
            InfoRow("Gender", client.gender)
            InfoRow("Height", client.heightCm?.let { "$it cm" } ?: "-")
            InfoRow("Address", client.address)
            InfoRow("Occupation", client.occupation)
            InfoRow("Goal", client.goal)
            InfoRow("Consultant", client.consultantName)
        }
        InfoSection("Lifestyle Information") {
            InfoRow("Wake-up Time", client.wakeUpTime)
            InfoRow("Exercise/Walk", if (client.exerciseOrWalk) "Yes" else "No")
            InfoRow("Water Intake", client.waterIntakeLiters?.let { "$it L" } ?: "-")
            InfoRow("Tea/Coffee", client.teaCoffeeCups?.let { "$it cups" } ?: "-")
            InfoRow("Diet Type", client.dietType)
            InfoRow("Breakfast", client.breakfast)
            InfoRow("Lunch", client.lunch)
            InfoRow("Evening Snack", client.eveningSnack)
            InfoRow("Dinner", client.dinner)
            InfoRow("Sleep Hours", client.sleepHours?.let { "$it hrs" } ?: "-")
        }
        InfoSection("Target") {
            InfoRow("Target Weight", client.targetWeight?.let { "$it kg" } ?: "-")
            InfoRow("Target Date", client.targetDate)
        }
    }
}

@Composable
private fun InfoSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 12.dp, bottom = 6.dp))
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), content = content)
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    if (value.isBlank()) return
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, modifier = Modifier.weight(0.4f), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, modifier = Modifier.weight(0.6f))
    }
}

@Composable
private fun VisitsTab(visits: List<Visit>, onDelete: (Visit) -> Unit) {
    if (visits.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No visits recorded yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(visits, key = { it.id }) { visit ->
            VisitCard(visit, onDelete = { onDelete(visit) })
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun VisitCard(visit: Visit, onDelete: () -> Unit) {
    val df = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Visit ${visit.visitNumber}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error) }
            }
            Text(df.format(Date(visit.date)), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(6.dp))
            FlowRowMetrics(visit)
        }
    }
}

@Composable
private fun FlowRowMetrics(v: Visit) {
    val metrics = listOfNotNull(
        v.weightKg?.let { "Weight: $it kg" },
        v.bmi?.let { "BMI: $it" },
        v.bodyFatPercent?.let { "Body Fat: $it%" },
        v.musclePercent?.let { "Muscle: $it%" },
        v.waistCm?.let { "Waist: $it cm" },
        v.hipCm?.let { "Hip: $it cm" }
    )
    Row(Modifier.horizontalScroll(rememberScrollState())) {
        metrics.forEach {
            AssistChip(onClick = {}, label = { Text(it) }, modifier = Modifier.padding(end = 6.dp))
        }
    }
}

@Composable
private fun ChartTab(visits: List<Visit>) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        if (visits.size < 2) {
            Text("Add at least 2 visits to see progress charts.", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 24.dp))
            return@Column
        }
        Text("Weight Progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        WeightTrendChart(visits = visits, metricLabel = "Weight (kg)") { it.weightKg }

        Spacer(Modifier.height(24.dp))
        Text("BMI Progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        WeightTrendChart(visits = visits, metricLabel = "BMI") { it.bmi }

        Spacer(Modifier.height(24.dp))
        Text("Body Fat % Progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        WeightTrendChart(visits = visits, metricLabel = "Body Fat %") { it.bodyFatPercent }
    }
}
