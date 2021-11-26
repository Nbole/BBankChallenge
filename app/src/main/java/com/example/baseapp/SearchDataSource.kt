package com.example.baseapp

interface SearchDataSource {
    suspend fun getPreviewHomeSupplierResponse(
        baseUrl: String,
        queryBody: String,
        input: CoordinatesInput,
    ): GraphQlResponse<PreviewHomeSupplierResponse?>
}