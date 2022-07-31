package com.betulnecanli.todomvvm.util

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
    return value?.let { LocalDateTime.parse(it) }
}

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}