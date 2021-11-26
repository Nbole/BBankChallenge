package com.example.baseapp

import com.example.baseapp.db.MovieResponse
import retrofit2.Response

interface MovieDataSource {
    suspend fun getLatestMovies(): Response<MovieResponse>
}
