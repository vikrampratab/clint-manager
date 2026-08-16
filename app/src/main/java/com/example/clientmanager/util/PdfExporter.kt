package com.example.clientmanager.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.print.PrintAttributes
import android.print.PrintManager
import androidx.core.content.FileProvider
import com.example.clientmanager.data.Client
import com.example.clientmanager.data.ProgressNote
import com.example.clientmanager.data.Visit
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Renders a single client's full record (personal info, lifestyle info,
 * body-assessment visit history table, and progress notes) into a
 * multi-page A4 PDF that mirrors the printed clinic form.
 */
object PdfExporter {

    private const val PAGE_WIDTH = 595   // A4 at 72dpi
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 32f

    fun exportClientReport(context: Context, client: Client, visits: List<Visit>, notes: List<ProgressNote>): File {
        val document = PdfDocument()
        val titlePaint = Paint().apply { textSize = 18f; isFakeBoldText = true; color = Color.rgb(27, 94, 32) }
        val sectionPaint = Paint().apply { textSize = 13f; isFakeBoldText = true; color = Color.rgb(27, 94, 32) }
        val labelPaint = Paint().apply { textSize = 10f; color = Color.DKGRAY }
        val valuePaint = Paint().apply { textSize = 10f; color = Color.BLACK }
        val linePaint = Paint().apply { color = Color.LTGRAY; strokeWidth = 1f }
        val tableHeaderPaint = Paint().apply { textSize = 8.5f; isFakeBoldText = true; color = Color.WHITE }
        val tableHeaderBg = Paint().apply { color = Color.rgb(27, 94, 32) }
        val tableCellPaint = Paint().apply { textSize = 8f; color = Color.BLACK }

        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas
        var y = MARGIN

        fun newPage() {
            document.finishPage(page)
            pageNumber++
            pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
            page = document.startPage(pageInfo)
            canvas = page.canvas
            y = MARGIN
        }

        fun ensureSpace(needed: Float) {
            if (y + needed > PAGE_HEIGHT - MARGIN) newPage()
        }

        // Header
        canvas.drawText("GAURAV WELLNESS CENTRE", MARGIN, y + 16, titlePaint)
        y += 22
        canvas.drawText("Client Health Record", MARGIN, y + 12, Paint().apply { textSize = 11f; color = Color.GRAY })
        y += 24
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
        y += 18

        // Personal Information
        canvas.drawText("PERSONAL INFORMATION", MARGIN, y, sectionPaint)
        y += 16
        val personalRows = listOf(
            "Name" to client.name,
            "Mobile No." to client.mobileNo,
            "DOB" to client.dob,
            "Age" to (client.age?.toString() ?: ""),
            "Gender" to client.gender,
            "Height" to (client.heightCm?.let { "$it cm" } ?: ""),
            "Address" to client.address,
            "Occupation" to client.occupation,
            "Goal" to client.goal,
            "Consultant" to client.consultantName
        )
        personalRows.forEach { (label, value) ->
            if (value.isBlank()) return@forEach
            ensureSpace(14f)
            canvas.drawText("$label:", MARGIN, y, labelPaint)
            canvas.drawText(value, MARGIN + 110, y, valuePaint)
            y += 14
        }

        y += 8
        canvas.drawText("LIFESTYLE INFORMATION", MARGIN, y, sectionPaint)
        y += 16
        val lifestyleRows = listOf(
            "Wake-up Time" to client.wakeUpTime,
            "Exercise/Walk" to if (client.exerciseOrWalk) "Yes" else "No",
            "Water Intake" to (client.waterIntakeLiters?.let { "$it L/day" } ?: ""),
            "Tea/Coffee" to (client.teaCoffeeCups?.let { "$it cups/day" } ?: ""),
            "Diet Type" to client.dietType,
            "Breakfast" to client.breakfast,
            "Lunch" to client.lunch,
            "Evening Snack" to client.eveningSnack,
            "Dinner" to client.dinner,
            "Sleep Hours" to (client.sleepHours?.let { "$it hrs" } ?: "")
        )
        lifestyleRows.forEach { (label, value) ->
            if (value.isBlank()) return@forEach
            ensureSpace(14f)
            canvas.drawText("$label:", MARGIN, y, labelPaint)
            canvas.drawText(value, MARGIN + 110, y, valuePaint)
            y += 14
        }

        // Body Assessment Tracker table
        if (visits.isNotEmpty()) {
            y += 10
            ensureSpace(30f)
            canvas.drawText("BODY ASSESSMENT TRACKER", MARGIN, y, sectionPaint)
            y += 14

            val params = listOf(
                "Date" to { v: Visit -> SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date(v.date)) },
                "Weight(kg)" to { v: Visit -> v.weightKg?.toString() ?: "-" },
                "BMI" to { v: Visit -> v.bmi?.toString() ?: "-" },
                "Body Fat%" to { v: Visit -> v.bodyFatPercent?.toString() ?: "-" },
                "Muscle%" to { v: Visit -> v.musclePercent?.toString() ?: "-" },
                "Waist" to { v: Visit -> v.waistCm?.toString() ?: "-" },
                "Hip" to { v: Visit -> v.hipCm?.toString() ?: "-" }
            )
            val colWidth = (PAGE_WIDTH - 2 * MARGIN) / (params.size + 1)

            // header row
            ensureSpace(20f)
            val rowH = 16f
            canvas.drawRect(MARGIN, y - 11, PAGE_WIDTH - MARGIN, y + 5, tableHeaderBg)
            canvas.drawText("Visit", MARGIN + 2, y, tableHeaderPaint)
            params.forEachIndexed { i, (label, _) ->
                canvas.drawText(label, MARGIN + colWidth * (i + 1) + 2, y, tableHeaderPaint)
            }
            y += rowH

            visits.forEach { visit ->
                ensureSpace(rowH)
                canvas.drawText("V${visit.visitNumber}", MARGIN + 2, y, tableCellPaint)
                params.forEachIndexed { i, (_, fn) ->
                    canvas.drawText(fn(visit), MARGIN + colWidth * (i + 1) + 2, y, tableCellPaint)
                }
                canvas.drawLine(MARGIN, y + 4, PAGE_WIDTH - MARGIN, y + 4, linePaint)
                y += rowH
            }
        }

        // Progress notes
        if (notes.isNotEmpty()) {
            y += 14
            ensureSpace(30f)
            canvas.drawText("PROGRESS NOTES", MARGIN, y, sectionPaint)
            y += 16
            val df = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            notes.forEach { note ->
                ensureSpace(40f)
                canvas.drawText(df.format(Date(note.date)), MARGIN, y, labelPaint)
                y += 12
                if (note.dietChanges.isNotBlank()) { canvas.drawText("Diet: ${note.dietChanges}", MARGIN + 10, y, valuePaint); y += 12 }
                if (note.exerciseChanges.isNotBlank()) { canvas.drawText("Exercise: ${note.exerciseChanges}", MARGIN + 10, y, valuePaint); y += 12 }
                if (note.remarks.isNotBlank()) { canvas.drawText("Remarks: ${note.remarks}", MARGIN + 10, y, valuePaint); y += 12 }
                y += 6
            }
        }

        document.finishPage(page)

        val fileName = "${client.name.replace(" ", "_")}_report_${System.currentTimeMillis()}.pdf"
        val outDir = File(context.getExternalFilesDir(null), "pdfs").apply { mkdirs() }
        val outFile = File(outDir, fileName)
        FileOutputStream(outFile).use { document.writeTo(it) }
        document.close()
        return outFile
    }

    fun getUriForFile(context: Context, file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    /** Sends the given PDF to the Android system print dialog. */
    fun printPdf(context: Context, file: File, jobName: String) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val adapter = object : android.print.PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: android.os.CancellationSignal?,
                callback: LayoutResultCallback?,
                extras: android.os.Bundle?
            ) {
                if (cancellationSignal?.isCanceled == true) {
                    callback?.onLayoutCancelled(); return
                }
                val info = android.print.PrintDocumentInfo.Builder(file.name)
                    .setContentType(android.print.PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build()
                callback?.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<out android.print.PageRange>?,
                destination: android.os.ParcelFileDescriptor?,
                cancellationSignal: android.os.CancellationSignal?,
                callback: WriteResultCallback?
            ) {
                try {
                    file.inputStream().use { input ->
                        destination?.fileDescriptor?.let { fd ->
                            FileOutputStream(fd).use { output -> input.copyTo(output) }
                        }
                    }
                    callback?.onWriteFinished(arrayOf(android.print.PageRange.ALL_PAGES))
                } catch (e: Exception) {
                    callback?.onWriteFailed(e.message)
                }
            }
        }
        printManager.print(jobName, adapter, PrintAttributes.Builder().build())
    }
}
