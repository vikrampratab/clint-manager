package com.example.clientmanager.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clientmanager.data.Visit
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf

/**
 * Line chart showing one metric (e.g. Weight, BMI, Body Fat %) across visits.
 * [selector] pulls the Double value out of a Visit; null values are skipped.
 */
@Composable
fun WeightTrendChart(
    visits: List<Visit>,
    metricLabel: String,
    selector: (Visit) -> Double?
) {
    val entries = remember(visits) {
        visits.mapIndexedNotNull { index, visit ->
            selector(visit)?.let { value -> entryOf(index.toFloat(), value.toFloat()) }
        }
    }

    if (entries.size < 2) return

    val producer = remember { ChartEntryModelProducer() }
    LaunchedEffect(entries) { producer.setEntries(entries) }

    Chart(
        chart = lineChart(),
        chartModelProducer = producer,
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(),
        modifier = Modifier.fillMaxWidth().height(220.dp)
    )
}
