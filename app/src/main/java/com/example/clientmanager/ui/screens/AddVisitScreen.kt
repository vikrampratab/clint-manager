package com.example.clientmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clientmanager.data.Visit
import com.example.clientmanager.ui.ClientDetailViewModel
import com.example.clientmanager.ui.RepoViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVisitScreen(
    clientId: Long,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val vm: ClientDetailViewModel = viewModel(
        factory = RepoViewModelFactory { ClientDetailViewModel(it, clientId) }
    )

    var weight by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf("") }
    var muscle by remember { mutableStateOf("") }
    var visceralFat by remember { mutableStateOf("") }
    var subFat by remember { mutableStateOf("") }
    var bmr by remember { mutableStateOf("") }
    var bodyAge by remember { mutableStateOf("") }
    var hydration by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var boneMass by remember { mutableStateOf("") }
    var metabolicAge by remember { mutableStateOf("") }
    var waist by remember { mutableStateOf("") }
    var hip by remember { mutableStateOf("") }
    var chest by remember { mutableStateOf("") }
    var arm by remember { mutableStateOf("") }
    var thigh by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Visit") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                "Body Assessment Tracker",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Date: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            NumField("Weight (kg)", weight) { weight = it }
            NumField("BMI", bmi) { bmi = it }
            NumField("Body Fat %", bodyFat) { bodyFat = it }
            NumField("Muscle %", muscle) { muscle = it }
            NumField("Visceral Fat", visceralFat) { visceralFat = it }
            NumField("Subcutaneous Fat", subFat) { subFat = it }
            NumField("BMR (kcal)", bmr) { bmr = it }
            NumField("Body Age", bodyAge) { bodyAge = it }
            NumField("Hydration %", hydration) { hydration = it }
            NumField("Protein %", protein) { protein = it }
            NumField("Bone Mass (kg)", boneMass) { boneMass = it }
            NumField("Metabolic Age", metabolicAge) { metabolicAge = it }
            NumField("Waist (cm)", waist) { waist = it }
            NumField("Hip (cm)", hip) { hip = it }
            NumField("Chest (cm)", chest) { chest = it }
            NumField("Arm (cm)", arm) { arm = it }
            NumField("Thigh (cm)", thigh) { thigh = it }

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    val visit = Visit(
                        clientId = clientId,
                        visitNumber = 0, // recalculated in ViewModel
                        date = System.currentTimeMillis(),
                        weightKg = weight.toDoubleOrNull(),
                        bmi = bmi.toDoubleOrNull(),
                        bodyFatPercent = bodyFat.toDoubleOrNull(),
                        musclePercent = muscle.toDoubleOrNull(),
                        visceralFat = visceralFat.toDoubleOrNull(),
                        subcutaneousFat = subFat.toDoubleOrNull(),
                        bmrKcal = bmr.toDoubleOrNull(),
                        bodyAge = bodyAge.toIntOrNull(),
                        hydrationPercent = hydration.toDoubleOrNull(),
                        proteinPercent = protein.toDoubleOrNull(),
                        boneMassKg = boneMass.toDoubleOrNull(),
                        metabolicAge = metabolicAge.toIntOrNull(),
                        waistCm = waist.toDoubleOrNull(),
                        hipCm = hip.toDoubleOrNull(),
                        chestCm = chest.toDoubleOrNull(),
                        armCm = arm.toDoubleOrNull(),
                        thighCm = thigh.toDoubleOrNull()
                    )
                    vm.addVisit(visit) { onSaved() }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Visit")
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NumField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
    )
}
