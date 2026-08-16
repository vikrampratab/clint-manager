package com.example.clientmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // --- Personal Information ---
    val name: String,
    val mobileNo: String,
    val dob: String = "",           // dd/mm/yyyy
    val age: Int? = null,
    val gender: String = "",        // Male / Female
    val heightCm: Double? = null,
    val address: String = "",
    val occupation: String = "",
    val goal: String = "",          // Weight Loss / Weight Gain / Fitness
    val consultantName: String = "",
    val registrationDate: Long = System.currentTimeMillis(),

    // --- Lifestyle Information ---
    val wakeUpTime: String = "",
    val exerciseOrWalk: Boolean = false,
    val waterIntakeLiters: Double? = null,
    val teaCoffeeCups: Int? = null,
    val dietType: String = "",      // Veg / Non-Veg
    val breakfast: String = "",
    val lunch: String = "",
    val eveningSnack: String = "",
    val dinner: String = "",
    val sleepHours: Double? = null,

    // --- Target Section ---
    val targetWeight: Double? = null,
    val targetDate: String = ""
)
