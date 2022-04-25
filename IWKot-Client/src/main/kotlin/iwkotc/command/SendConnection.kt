package iwkotc.command

import iwkotc.net.Mw2HttpClient
import iwkotc.tailer.parser.IWKotPayload
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

class SendConnection(private val payload : IWKotPayload) : Command {

    override fun execute() {
        val data = HashMap<String, String>()

        if(payload.data.contains(",")) {
            val payloadArray = payload.data.split(",")
            for(payload in payloadArray)
                parseKeyValue(data, payload)
        } else
            parseKeyValue(data, payload.data)

        val client = Mw2HttpClient(buildUrl(data), "40A40A3A099676C832C2E78688CE37E01FE0A4BF747FACEF2B75D7A58C668903").request()
        handleResponse(client.responseCode!!.toString())
    }

    override fun handleResponse(response : String) {
        println("Response Code: $response")
    }

    private fun parseKeyValue(data : HashMap<String, String>, payload : String) {
        if(!payload.contains("="))
            return
        val separator = payload.indexOf("=")
        val key = payload.substring(0, separator)
        val value = payload.substring(separator + 1)
        data[key] = value
    }

    private fun buildUrl(data : HashMap<String, String>) : URI {
        return UriComponentsBuilder.newInstance()
            .scheme("http") //HTTP for local testing
            .host("localhost").port(28600)
            .path("api/servers")
            .path("/").path(data.getValue("mapName"))
            .path("/").path("join")
            .queryParam("name", data.getValue("name"))
            .queryParam("guid", data.getValue("guid"))
            .queryParam("port", data.getValue("port"))
            .build().toUri()
    }

}