package com.example.baseapp

import android.util.Log
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.baseapp.db.MovieResponse
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import kotlin.reflect.KClass

class HttpRequestImpl @Inject constructor(): HttpRequest {
    override suspend fun get(url: String, params: String?, key: String?): MovieResponse? {
        return get(url,params,key)
    }
    override suspend fun previewSupplier(
        baseUrl: String,
        queryBody: String,
        variables: CoordinatesInput
    ): PreviewHomeSupplierResponse? {
        return query(baseUrl, "previewHomeSuppliers" , queryBody, variables)
    }
    override suspend fun getStatesByCountry(
        baseUrl: String,
        queryBody: String,
        input: String
    ): List<State>? {
        return query(baseUrl, "getStatesByCountry" , queryBody, input)
    }
}

// TODO: permitir que el método acepte múltiples atributos
// TODO: Crear una data class para indicar el estado de la request, similar a REQUEST de retrofit
inline fun <reified T> get(baseUrl: String, params: String?, key: String?): T? {
    val response: T?
    // TODO: resolver esto en un método
    val url = URL("$baseUrl?$params=$key")
    val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
    httpURLConnection.requestMethod = "GET"
    val inputStream: InputStream = httpURLConnection.inputStream
    val outPutStream = ByteArrayOutputStream()
    // TODO: resolver esto en un método aparte
    var temp = ByteArray(inputStream.available())
    while (inputStream.read(temp, 0, temp.size) != -1) {
        outPutStream.write(temp)
        temp = ByteArray(httpURLConnection.inputStream.available())
    }
    // TODO: Probar en una respuesta que sea una lista

    response = Gson().getAdapter(TypeToken.get(T::class.java)).fromJson(outPutStream.toString())

    inputStream.close()
    return response
}

// TODO: probar que funcione en múltiples querys
inline fun <reified T, reified R> query(
    baseUrl: String,
    queryName: String,
    queryBody: String,
    input: R?,
    variableName: String = "input"
): T? {
    val response: T?
    val className: String? = input?.getClass()?.simpleName
    val query = "query $queryName(\$$variableName: $className!){$queryName(input:\$input) $queryBody}"
    val httpURLConnection: HttpURLConnection = URL(baseUrl).openConnection() as HttpURLConnection
    httpURLConnection.requestMethod = "POST"
    httpURLConnection.setRequestProperty("content-type", "application/json")
    httpURLConnection.doOutput = true
    httpURLConnection.doInput = true
    val outputStreamWriter =
        OutputStreamWriter(BufferedOutputStream(httpURLConnection.outputStream))
    outputStreamWriter.write(
        "{\"query\":\"$query\",\"variables\":{\"input\":${Gson().toJson(input)}}}"
    )
    outputStreamWriter.flush()
    outputStreamWriter.close()
    httpURLConnection.connect()
    val inputStream: InputStream = httpURLConnection.inputStream
    val outPutStream = ByteArrayOutputStream()
    var aux = ByteArray(inputStream.available())

    while (inputStream.read(aux, 0, aux.size) != -1) {
        outPutStream.write(aux)
        aux = ByteArray(httpURLConnection.inputStream.available())
    }
    inputStream.close()
    response = Gson().getAdapter(TypeToken.get(T::class.java)).fromJson(
        JSONObject(JSONObject(outPutStream.toString()).optString("data")).optString(queryName)
    )
    val bodyLink = ("query (\$$variableName: $className!){ $queryName (input: \$input) $queryBody}" +
        " &variables= {\"input\":${Gson().toJson(input)}}")
    Log.d(
        "HttpRequest:Query",
        "https://bff-qa.wabi2b.com/graphiql?query=${formatLink(bodyLink)}"
    )
    return response
}

fun formatLink(baseLink: String): String =
    baseLink.replace(
        " ", "%20"
    ).replace(
        "$", "%24"
    ).replace(
        ":", "%3A"
    ).replace(
        "{", "%7B"
    ).replace(
        "}", "%7D"
    )

//interface GraphQlInput

//data class Input<T>(val input: T ):GraphQlInput

data class State(val id: String, val name: String)

data class CoordinatesInput(
    val lat: Double,
    val lng: Double,
)

data class MealResponse(
    @SerializedName("idCategory") val idCategory: String,
    @SerializedName("strCategory") val strCategory: String,
    @SerializedName("srtCategoryDescription") val srtCategoryDescription: String,
    @SerializedName("strCategoryThumb") val strCategoryThumb: String,
)

data class PreviewHomeSupplierRequest(val previewHomeSuppliers: PreviewHomeSupplierResponse)

data class PreviewHomeSupplierResponse(val suppliers: List<PreviewSupplier>?)

data class CountryConfigurationEntry(val key: String, val values: String)

data class Country(val id:String)

//data class CountryHomeInput(val locale: String): GraphQlInput

@Entity
data class PreviewHomeSupplierResult(
    @PrimaryKey val id: Int,
    @Embedded val suppliers: PreviewHomeSupplierResponse
)
data class PreviewSupplier(val id: Int, val name: String, val legalName: String, val avatar: String)
data class MealRequest(
    val categories: List<MealResponse>,
)

fun<T: Any> T.getClass(): KClass<out T> = this::class