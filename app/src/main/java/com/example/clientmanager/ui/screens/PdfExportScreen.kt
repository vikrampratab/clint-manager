package com.example.clientmanager.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clientmanager.ui.ClientDetailViewModel
import com.example.clientmanager.ui.RepoViewModelFactory
import com.example.clientmanager.util.PdfExporter
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfExportScreen(clientId: Long, onBack: () -> Unit) {
    val context = LocalContext.current
    val vm: ClientDetailViewModel = viewModel(factory = RepoViewModelFactory { ClientDetailViewModel(it, clientId) })
    val client by vm.client.collectAsState()
    val visits by vm.visits.collectAsState()
    val notes by vm.notes.collectAsState()

    var generatedFile by remember { mutableStateOf<File?>(null) }
    var isGenerating by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Export Report") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val c = client
            if (c == null) {
                CircularProgressIndicator()
                return@Column
            }

            Text("Generate a PDF report for ${c.name}", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                "Includes personal info, lifestyle info, all ${visits.size} visit(s) and ${notes.size} progress note(s).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    isGenerating = true
                    generatedFile = PdfExporter.exportClientReport(context, c, visits, notes)
                    isGenerating = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isGenerating) "Generating..." else "Generate PDF")
            }

            generatedFile?.let { file ->
                Spacer(Modifier.height(16.dp))
                Text("Report ready: ${file.name}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = {
                        val uri = PdfExporter.getUriForFile(context, file)
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/pdf"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Report"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Share")
                    }

                    Button(onClick = {
                        PdfExporter.printPdf(context, file, "${c.name} - Wellness Report")
                    }) {
                        Icon(Icons.Default.Print, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Print")
                    }
                }
            }
        }
    }
}
