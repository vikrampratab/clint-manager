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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clientmanager.data.Client
import com.example.clientmanager.ui.AddClientViewModel
import com.example.clientmanager.ui.RepoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClientScreen(
    onBack: () -> Unit,
    onSaved: (Long) -> Unit
) {
    val vm: AddClientViewModel = viewModel(factory = RepoViewModelFactory { AddClientViewModel(it) })

    // Personal Information
    var name by remember { mutableStateOf("") }
    var mobileNo by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var height by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("Weight Loss") }
    var consultantName by remember { mutableStateOf("") }

    // Lifestyle Information
    var wakeUpTime by remember { mutableStateOf("") }
    var exercise by remember { mutableStateOf(false) }
    var waterIntake by remember { mutableStateOf("") }
    var teaCoffee by remember { mutableStateOf("") }
    var dietType by remember { mutableStateOf("Veg") }
    var breakfast by remember { mutableStateOf("") }
    var lunch by remember { mutableStateOf("") }
    var eveningSnack by remember { mutableStateOf("") }
    var dinner by remember { mutableStateOf("") }
    var sleepHours by remember { mutableStateOf("") }

    // Target
    var targetWeight by remember { mutableStateOf("") }
    var targetDate by remember { mutableStateOf("") }

    val nameError = name.isBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Client") },
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
            SectionHeader("Personal Information")
            LabeledField("Name *", name) { name = it }
            LabeledField("Mobile No.", mobileNo, KeyboardType.Phone) { mobileNo = it }
            Row {
                Box(Modifier.weight(1f)) { LabeledField("DOB (dd/mm/yyyy)", dob) { dob = it } }
                Spacer(Modifier.width(8.dp))
                Box(Modifier.weight(1f)) { LabeledField("Age", age, KeyboardType.Number) { age = it } }
            }
            SegmentedChoice("Gender", listOf("Male", "Female"), gender) { gender = it }
            LabeledField("Height (cm)", height, KeyboardType.Number) { height = it }
            LabeledField("Address", address, singleLine = false) { address = it }
            LabeledField("Occupation", occupation) { occupation = it }
            SegmentedChoice("Goal", listOf("Weight Loss", "Weight Gain", "Fitness"), goal) { goal = it }
            LabeledField("Consultant Name", consultantName) { consultantName = it }

            Spacer(Modifier.height(20.dp))
            SectionHeader("Lifestyle Information")
            LabeledField("Wake-up Time", wakeUpTime) { wakeUpTime = it }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                Text("Exercise or Walk", modifier = Modifier.weight(1f))
                Switch(checked = exercise, onCheckedChange = { exercise = it })
            }
            LabeledField("Water Intake (Liters)", waterIntake, KeyboardType.Number) { waterIntake = it }
            LabeledField("Tea/Coffee (Cups)", teaCoffee, KeyboardType.Number) { teaCoffee = it }
            SegmentedChoice("Diet Type", listOf("Veg", "Non-Veg"), dietType) { dietType = it }
            LabeledField("Breakfast", breakfast) { breakfast = it }
            LabeledField("Lunch", lunch) { lunch = it }
            LabeledField("Evening Snack", eveningSnack) { eveningSnack = it }
            LabeledField("Dinner", dinner) { dinner = it }
            LabeledField("Sleep Hours", sleepHours, KeyboardType.Number) { sleepHours = it }

            Spacer(Modifier.height(20.dp))
            SectionHeader("Target Section")
            LabeledField("Target Weight (kg)", targetWeight, KeyboardType.Number) { targetWeight = it }
            LabeledField("Target Date (dd/mm/yyyy)", targetDate) { targetDate = it }

            if (nameError) {
                Text("Name is required", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    val client = Client(
                        name = name.trim(),
                        mobileNo = mobileNo.trim(),
                        dob = dob.trim(),
                        age = age.toIntOrNull(),
                        gender = gender,
                        heightCm = height.toDoubleOrNull(),
                        address = address.trim(),
                        occupation = occupation.trim(),
                        goal = goal,
                        consultantName = consultantName.trim(),
                        wakeUpTime = wakeUpTime.trim(),
                        exerciseOrWalk = exercise,
                        waterIntakeLiters = waterIntake.toDoubleOrNull(),
                        teaCoffeeCups = teaCoffee.toIntOrNull(),
                        dietType = dietType,
                        breakfast = breakfast.trim(),
                        lunch = lunch.trim(),
                        eveningSnack = eveningSnack.trim(),
                        dinner = dinner.trim(),
                        sleepHours = sleepHours.toDoubleOrNull(),
                        targetWeight = targetWeight.toDoubleOrNull(),
                        targetDate = targetDate.trim()
                    )
                    vm.addClient(client) { id -> onSaved(id) }
                },
                enabled = !nameError,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Client")
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 10.dp)
    )
    Divider(modifier = Modifier.padding(bottom = 10.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LabeledField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = singleLine,
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
    )
}

@Composable
private fun SegmentedChoice(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column(Modifier.padding(vertical = 6.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(4.dp))
        Row {
            options.forEach { opt ->
                FilterChip(
                    selected = selected == opt,
                    onClick = { onSelect(opt) },
                    label = { Text(opt) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
