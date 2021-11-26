package com.example.baseapp

import com.example.baseapp.db.MovieResponse
import retrofit2.Response
import javax.inject.Inject

class MovieDataSourceImpl @Inject constructor(
    private val movieService: MovieService
) : MovieDataSource {
    override suspend fun getLatestMovies(): Response<MovieResponse> =
        movieService.getLatestMovies("5e30e8afd06d2b8b9aae8eb164c85a29")
}
