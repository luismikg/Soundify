package com.luis.soundify.core

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun String.toReadableDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = inputFormat.parse(this)

        val calendar = Calendar.getInstance().apply { time = date!! }

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val monthName = when (month) {
            0 -> "Enero"
            1 -> "Febrero"
            2 -> "Marzo"
            3 -> "Abril"
            4 -> "Mayo"
            5 -> "Junio"
            6 -> "Julio"
            7 -> "Agosto"
            8 -> "Septiembre"
            9 -> "Octubre"
            10 -> "Noviembre"
            11 -> "Diciembre"
            else -> ""
        }

        "$day, $monthName $year"
    } catch (e: Exception) {
        this
    }
}