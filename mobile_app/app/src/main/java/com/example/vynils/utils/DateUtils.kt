package com.example.vynils.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    private val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun formatRetrofitDate(dateString: String?): String {
        if (dateString.isNullOrBlank()) return ""
        return try {
            val date = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateString
        }
    }
}