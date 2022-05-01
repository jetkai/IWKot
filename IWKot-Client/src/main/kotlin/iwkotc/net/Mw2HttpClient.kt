package iwkotc.net

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * Mw2HttpClient
 *
 * Used for test classes
 *
 * @author Kai
 * @version 1.0, 24/04/2022
 */
class Mw2HttpClient(private val uri : URI, private val hash : String, private val auth : String) {

    private val client = buildClient()
    private val builder = buildHeaders()

    var responseBody : String ?= null
    var responseCode : Int ?= -1

    fun request() : Mw2HttpClient {
        val request = builder.uri(uri).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        responseBody = response.body()
        responseCode = response.statusCode()
        return this
    }

    private fun buildClient(): HttpClient {
        return HttpClient.newBuilder().build()
    }

    private fun buildHeaders() : HttpRequest.Builder {
        val builder = HttpRequest.newBuilder()
        builder.header("Content-Type", "application/json")
        builder.header("x-secret-hash", hash)
        builder.header("Authorization", auth)
        builder.header("User-Agent", "Mw2-Server/1.0")
        return builder
    }


}