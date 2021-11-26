package com.example.baseapp

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun get(): ByteArray {
    val response: HttpResponse = HttpClient().request("") {
        method = HttpMethod.Get
    }
    return response.receive()
}