package com.example.clientmanager.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clientmanager.data.Client
import com.example.clientmanager.data.Visit
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun ClientComparisonBarChart(clients: List<Client>, latestVisits: List<Visit>) {
    val weightByClientId = remember(latestVisits) { latestVisits.associateBy { it.clientId } }
    val labeled = remember(clients, weightByClientId) {
        clients.mapNotNull { c -> weightByClientId[c.id]?.weightKg?.let { w -> c.name to w } }
    }

    if (labeled.isEmpty()) return

    val producer = remember { ChartEntryModelProducer() }
    LaunchedEffect(labeled) {
        producer.setEntries(labeled.mapIndexed { i, (_, w) -> entryOf(i.toFloat(), w.toFloat()) })
    }

    val labelFormatter = remember(labeled) {
        AxisValueFormatter<com.patrykandpatrick.vico.core.axis.AxisPosition.Horizontal.Bottom> { value, _ ->
            labeled.getOrNull(value.toInt())?.first ?: ""
        }
    }

    Chart(
        chart = columnChart(),
        chartModelProducer = producer,
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(valueFormatter = labelFormatter),
        modifier = Modifier.fillMaxWidth().height(240.dp)
    )
}
