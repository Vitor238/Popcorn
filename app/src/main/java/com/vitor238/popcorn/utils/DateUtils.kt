package com.vitor238.popcorn.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun parseDate(date: String?): String {
        val tmdbPattern = "yyyy-MM-dd"
        val localizedDateFormat: DateFormat =
            DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault())
        val originalDateFormat = SimpleDateFormat(tmdbPattern, Locale.ENGLISH)

        return try {
            val parsedDate = originalDateFormat.parse(date!!)
            localizedDateFormat.format(parsedDate!!)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}