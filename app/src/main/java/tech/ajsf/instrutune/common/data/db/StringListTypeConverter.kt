package tech.ajsf.instrutune.common.data.db

import androidx.room.TypeConverter

class StringListTypeConverter {

    @TypeConverter
    fun stringToStringList(data: String): List<String> =
        if (data.isNotBlank()) data.split(",") else emptyList()

    @TypeConverter
    fun stringListToString(list: List<String>): String = list.joinToString(",")
}