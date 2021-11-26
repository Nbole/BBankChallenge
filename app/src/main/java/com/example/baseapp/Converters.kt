package com.example.baseapp

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun restorePreview(listOfString: String): List<PreviewSupplier> =
        Gson().fromJson(listOfString, object : TypeToken<List<PreviewSupplier>>() {}.type)

    @TypeConverter
    fun savePreview(list: List<PreviewSupplier>): String = Gson().toJson(list)
}