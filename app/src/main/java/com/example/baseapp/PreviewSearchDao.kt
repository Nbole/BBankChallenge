package com.example.baseapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.baseapp.db.PagedMovies
import kotlinx.coroutines.flow.Flow

@Dao
interface PreviewSearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePreviewHomeSupplierResult(input: PreviewHomeSupplierResult)

    @Query("SELECT * FROM PreviewHomeSupplierResult")
    fun loadPreviewHomeSupplierResult(): Flow<PreviewHomeSupplierResult>
}
