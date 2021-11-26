package com.example.baseapp

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.baseapp.db.PagedMovies

@Database(entities = [PagedMovies::class, PreviewHomeSupplierResult::class ], version = 2)
@TypeConverters(Converters::class)
abstract class SwarmDb : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun previewDao(): PreviewSearchDao

    companion object {
        const val DATABASE_NAME: String = "base_app"
    }
}
