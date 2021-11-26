package com.example.baseapp

import javax.inject.Inject

class SearchDataSourceImpl @Inject constructor(
     private  val searchService: SearchService
): SearchDataSource {
    override suspend fun getPreviewHomeSupplierResponse(
        baseUrl: String,
        queryBody: String,
        input: CoordinatesInput,
    ): GraphQlResponse<PreviewHomeSupplierResponse?> {
       return searchService.getPreviewHomeSupplierResponse(
            baseUrl = baseUrl,
            queryBody = queryBody,
            variables = input
        )
    }
}
