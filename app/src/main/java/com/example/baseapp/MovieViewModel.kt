package com.example.baseapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.ExperimentalPagingApi
import com.example.baseapp.db.PagedMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
   private val movieRepository: MovieRepository
) : ViewModel() {
    @ExperimentalPagingApi
    val latestMovies: LiveData<Resource<List<PagedMovies>>> =
        movieRepository.getLatestMovies().asLiveData()

    fun previewSearch(
        input: CoordinatesInput,
        body: String,
        baseUrl: String,
    ): LiveData<Resource<PreviewHomeSupplierResult>> =
        movieRepository.getPreviewSearch(
            input = input,
            baseUrl = baseUrl,
            body = body
        ).asLiveData()
}
