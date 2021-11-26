package com.example.baseapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PagedMovies(
    @PrimaryKey val id: Int,
    @SerializedName("original_title") val title: String?,
    val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
)
