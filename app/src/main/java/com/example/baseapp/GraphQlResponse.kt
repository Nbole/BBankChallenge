package com.example.baseapp

sealed class GraphQlResponse<T>{
    data class Success<T>(val t: T): GraphQlResponse<T>()
    data class Error<T>(val message: String?): GraphQlResponse<T>()
}
