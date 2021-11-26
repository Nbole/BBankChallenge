package com.example.baseapp

import com.example.baseapp.db.PagedMovies
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val db: MovieDao,
    private val dbPreview: PreviewSearchDao,
    private val movieDataSource: MovieDataSource,
    private val searchDataSource: SearchDataSource
) {
    fun getLatestMovies(): Flow<Resource<List<PagedMovies>>> = networkBoundResource(
        { db.loadMovies() },
        { movieDataSource.getLatestMovies() },
        { response ->
            val movies = response.body()?.results
            if (!movies.isNullOrEmpty()) {
                db.saveMovies(movies)
            }
        }
    )

    fun getPreviewSearch(
        input: CoordinatesInput,
        body: String,
        baseUrl: String,
    ): Flow<Resource<PreviewHomeSupplierResult>> = graphQlNetworkBoundResource(
        { dbPreview.loadPreviewHomeSupplierResult() },
        { searchDataSource.getPreviewHomeSupplierResponse(baseUrl, body, input) },
        { response ->
            if (response is GraphQlResponse.Success && response.t != null) {
                dbPreview.savePreviewHomeSupplierResult(
                    PreviewHomeSupplierResult(
                        Pair(input, body).hashCode(),
                        response.t
                    )
                )
            }
        }
    )
}
