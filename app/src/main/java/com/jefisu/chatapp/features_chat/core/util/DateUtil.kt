package com.jefisu.chatapp.features_chat.core.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale

object DateUtil {
    fun timeOfDay(): String {
        return when (LocalDateTime.now().hour) {
            in 0..11 -> "Good morning,"
            in 12..17 -> "Good afternoon,"
            else -> "Good evening,"
        }
    }

    fun toHour(timestamp: Long): String {
        return SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(timestamp)
    }

    fun getDateExt(timestamp: Long): Int {
        return SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            .format(timestamp)
            .toInt()
    }

    fun getLastMessageTime(timestamp: Long): String {
        val messageDate = getDateExt(timestamp)
        val currentDate = getDateExt(System.currentTimeMillis())
        return when {
            currentDate == messageDate -> toHour(timestamp)
            (currentDate - messageDate) == 1 -> "Yesterday"
            else -> DateFormat
                .getDateInstance(DateFormat.DATE_FIELD)
                .format(timestamp)
        }
    }

    fun getDateByMessages(timestamp: Long): String {
        val currentDate = getDateExt(System.currentTimeMillis())
        val messageDate = getDateExt(timestamp)
        return when {
            currentDate == messageDate -> "Today"
            (currentDate - messageDate) == 1 -> "Yesterday"
            else -> DateFormat.getDateInstance().format(timestamp)
        }
    }
}