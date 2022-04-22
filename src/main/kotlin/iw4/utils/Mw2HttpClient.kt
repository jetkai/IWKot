package iw4.utils

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
 * @version 1.0, 20/04/2022
 */
class Mw2HttpClient(private val uri : URI, private val hash : String) {

    private val client = buildClient()
    private val builder = buildHeaders()

    fun requestBody() : String {
        println("URI: $uri")
        val request = builder.uri(uri).build()
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body()
    }

    private fun buildClient(): HttpClient {
        return HttpClient.newBuilder().build()
    }

    private fun buildHeaders() : HttpRequest.Builder {
        val builder = HttpRequest.newBuilder()
        builder.header("Content-Type", "application/json")
        builder.header("x-secret-hash", hash)
        builder.header("User-Agent", "Mw2-Server/1.0")
        return builder
    }

}