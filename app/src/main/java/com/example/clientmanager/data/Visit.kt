package com.example.clientmanager.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One row = one "VISIT" in the Body Assessment Tracker table on the form.
 * A client can have many visits (VISIT 1, VISIT 2, ... VISIT N).
 */
@Entity(
    tableName = "visits",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("clientId")]
)
data class Visit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clientId: Long,
    val visitNumber: Int,           // 1, 2, 3 ...
    val date: Long,                 // visit date

    // --- Body Assessment Tracker parameters (exact fields from the form) ---
    val weightKg: Double? = null,
    val bmi: Double? = null,
    val bodyFatPercent: Double? = null,
    val musclePercent: Double? = null,
    val visceralFat: Double? = null,
    val subcutaneousFat: Double? = null,
    val bmrKcal: Double? = null,
    val bodyAge: Int? = null,
    val hydrationPercent: Double? = null,
    val proteinPercent: Double? = null,
    val boneMassKg: Double? = null,
    val metabolicAge: Int? = null,
    val waistCm: Double? = null,
    val hipCm: Double? = null,
    val chestCm: Double? = null,
    val armCm: Double? = null,
    val thighCm: Double? = null
)
