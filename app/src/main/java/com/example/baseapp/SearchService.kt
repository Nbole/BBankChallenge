package com.example.baseapp

interface SearchService {
    suspend fun getPreviewHomeSupplierResponse(
        baseUrl: String,
        queryBody: String,
        variables: CoordinatesInput
    ): GraphQlResponse<PreviewHomeSupplierResponse?>
}
