package com.example.baseapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.baseapp.db.PagedMovies
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovies(pagedMovies: List<PagedMovies>)

    @Query("SELECT * FROM PagedMovies")
    fun loadMovies(): Flow<List<PagedMovies>>
}
