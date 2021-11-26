package com.example.baseapp

import com.example.baseapp.db.MovieResponse

interface HttpRequest {
    suspend fun get(url: String, params: String?, key: String?): MovieResponse?
    suspend fun previewSupplier(
        baseUrl: String,
        queryBody: String,
        variables: CoordinatesInput,
    ): PreviewHomeSupplierResponse?
    suspend fun getStatesByCountry(
        baseUrl: String,
        queryBody: String,
        input: String,
    ): List<State>?
}
