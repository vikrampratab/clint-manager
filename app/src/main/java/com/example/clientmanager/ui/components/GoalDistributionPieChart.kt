package com.example.clientmanager.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.clientmanager.data.Client
import com.example.clientmanager.ui.theme.ChartColor1
import com.example.clientmanager.ui.theme.ChartColor2
import com.example.clientmanager.ui.theme.ChartColor3
import com.example.clientmanager.ui.theme.ChartColor4

@Composable
fun GoalDistributionPieChart(clients: List<Client>) {
    val counts = remember(clients) {
        clients.groupingBy { it.goal.ifBlank { "Not Set" } }.eachCount()
    }
    if (counts.isEmpty()) return

    val palette = listOf(ChartColor1, ChartColor2, ChartColor3, ChartColor4)
    val total = counts.values.sum().toFloat()
    val slices = counts.entries.mapIndexed { i, (label, count) ->
        Triple(label, count, palette[i % palette.size])
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(160.dp)) {
            var startAngle = -90f
            slices.forEach { (_, count, color) ->
                val sweep = (count / total) * 360f
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                    size = Size(size.width, size.height)
                )
                startAngle += sweep
            }
        }
        Spacer(Modifier.width(16.dp))
        Column {
            slices.forEach { (label, count, color) ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
                    Box(Modifier.size(12.dp).background(color, shape = androidx.compose.foundation.shape.CircleShape))
                    Spacer(Modifier.width(8.dp))
                    Text("$label ($count)", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
