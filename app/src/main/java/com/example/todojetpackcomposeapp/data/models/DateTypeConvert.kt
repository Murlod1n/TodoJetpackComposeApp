package com.example.todojetpackcomposeapp.data.models

import androidx.room.TypeConverter
import java.util.Date


class DateTypeConvert {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? {
        return date?.time
    }
}