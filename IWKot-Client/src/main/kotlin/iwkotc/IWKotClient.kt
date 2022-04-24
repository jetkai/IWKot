package iwkotc

import iwkotc.net.Mw2HttpClient
import java.net.URI

fun main() {
    val client = Mw2HttpClient(URI.create("http://localhost:28600"), "1234")
    println(client.requestBody())
}