//package com.emirsoylemez.hukukasistan.util
//
//import java.text.SimpleDateFormat
//import java.util.*
//import java.util.concurrent.TimeUnit
//
//fun calculateRemainingDays(dateString: String): Long {
//    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
//    val hearingDate = sdf.parse(dateString) ?: return 0
//    val today = Date()
//
//    val diffMillis = hearingDate.time - today.time
//    return TimeUnit.MILLISECONDS.toDays(diffMillis)
//}

package com.emirsoylemez.hukukasistan.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun calculateRemainingDays(dateString: String?): Long {
    // 1. Gelen veri tamamen boş veya null ise hiç hesaplamaya girme
    if (dateString.isNullOrBlank()) {
        return -1L // Hata/Belirsiz durumunu temsil etmesi için -1 döndürüyoruz
    }

    return try {
        // 2. Format uyuşmazlığını önlemek için (Nokta veya eğik çizgi fark etmeksizin çalışsın)
        val normalizedDate = dateString.replace(".", "/")
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val hearingDate = sdf.parse(normalizedDate) ?: return -1L
        val today = Date()

        val diffMillis = hearingDate.time - today.time
        TimeUnit.MILLISECONDS.toDays(diffMillis)

    } catch (e: Exception) {
        // 3. Tarih formatı tamamen anlamsızsa uygulamanın çökmesini engelle
        -1L
    }
}
