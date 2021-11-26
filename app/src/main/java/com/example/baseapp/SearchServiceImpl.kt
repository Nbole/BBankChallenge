package com.example.baseapp

import javax.inject.Inject

class SearchServiceImpl @Inject constructor(
   private val httpRequest: HttpRequest
): SearchService {

    override suspend fun getPreviewHomeSupplierResponse(
        baseUrl: String,
        queryBody: String,
        variables: CoordinatesInput,
    ): GraphQlResponse<PreviewHomeSupplierResponse?> {
       return httpRequest.previewSupplier(baseUrl, queryBody, variables)?.let {
           GraphQlResponse.Success(it)
       } ?: GraphQlResponse.Error(null)
    }
}
