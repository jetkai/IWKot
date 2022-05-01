package iwkotc.command

import iwkotc.net.Mw2HttpClient
import iwkotc.reflection.Factory
import iwkotc.reflection.Reflection
import iwkotc.reflection.hook.CommandHook
import iwkotc.tailer.parser.IWKotPayload
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

/**
 * PlayerConnection
 *
 * @author Kai
 * @version 1.0, 01/05/2022
 */
class PlayerConnection : Reflection, CommandHook {

    override fun init() {
        //List of commands that'll be used in the PlayerConnection class
        val commands = arrayOf("join", "leave", "ban", "unban")
        for(command in commands) {
            Factory.registerCommand(this, command)
        }
    }

    override fun execute(payload : IWKotPayload) : Boolean {
        val map = HashMap<String, Any>()
        map["command"] = payload.command

        if(payload.data.contains(",")) {
            val dataArray = payload.data.split(",")
            for(data in dataArray)
                parseKeyValue(map, data)
        } else
            parseKeyValue(map, payload.data)

        //Testing data - Nothing interesting here
        val hash = "40A40A3A099676C832C2E78688CE37E01FE0A4BF747FACEF2B75D7A58C668903"
        val auth = "Basic bXcyX3NlcnZlcjpNVzJTRVJWRVI="

        val client = Mw2HttpClient(buildUrl(map), hash, auth).request()
        handleResponse(client.responseCode!!.toString())

        return true
    }

    private fun handleResponse(response : String) {
        println("Response Code: $response")
    }

    private fun parseKeyValue(data : HashMap<String, Any>, payload : String) {
        if(!payload.contains("="))
            return
        if(payload.indexOf("=") + 1 > payload.length)
            return
        val separator = payload.indexOf("=")
        val key = payload.substring(0, separator)
        val value = payload.substring(separator + 1)
        data[key] = value
    }

    private fun buildUrl(data : HashMap<String, Any>) : URI {
        return UriComponentsBuilder.newInstance()
            .scheme("http") //HTTP for local testing
            .host("localhost").port(28600)
            .path("api/servers")
            .path("/").path(data.getValue("mapName").toString())
            .path("/").path(data.getValue("command").toString())
            .queryParam("name", data.getValue("name"))
            .queryParam("guid", data.getValue("guid"))
            .queryParam("port", data.getValue("port"))
            .build().toUri()
    }

}