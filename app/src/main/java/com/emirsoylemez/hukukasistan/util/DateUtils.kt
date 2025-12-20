package com.emirsoylemez.hukukasistan.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun calculateRemainingDays(dateString: String): Long {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val hearingDate = sdf.parse(dateString) ?: return 0
    val today = Date()

    val diffMillis = hearingDate.time - today.time
    return TimeUnit.MILLISECONDS.toDays(diffMillis)
}
