package iwkots.controllers.action

import iwkots.database.PlayerRepository
import iwkots.database.request.ModifyPlayerEntity
import iwkots.utils.yaml.ApiProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * HandleConnection Class
 *
 * Used as a RestController for API Queries
 * @see onPlayerConnection
 *
 * @author Kai
 * @version 1.1, 24/04/2022
 */
@RestController
class HandleConnection(playerRepository : PlayerRepository, private val properties : ApiProperties) {

    private data class Params(var mapName : String, var action : String, var name : String,
                              var guid : String, var guids : List<String>?, var port : Int)

    private lateinit var parameter : Params

    //Checks if player exists in database to modify existing data, otherwise creates them
    private final val modifyPlayerEntity = ModifyPlayerEntity(playerRepository)

    /**
     * This function will modify/update the Player within the SQL Database
     * @since version 1.0
     *
     * @param mapName The name of the map that requires to be updated
     * @param name The display name of the player
     * @param guid The guid of the player
     * @param port The port of the server that the player is connected to
     *
     * @return 200 - OK (in plaintext)
     * @throws 417 - FAILED TO SAVE PLAYER (in plaintext)
     */
    @RequestMapping(value = ["/api/servers/{mapName}/{action}"],
        produces = ["text/plain; charset=utf-8"], method = [RequestMethod.GET])
    fun onPlayerConnection(@PathVariable mapName : String,
                           @PathVariable action : String,
                           @RequestParam(value = "name", required = false) name : String,
                           @RequestParam(value = "guid", required = false) guid : String,
                           @RequestParam(value = "guids", required = false) guids : List<String>?,
                           @RequestParam(value = "port", required = false, defaultValue = "0") port : Int,
                           request : HttpServletRequest) : ResponseEntity<Any> {

        //TODO clean this up (could use request.params/variable -> mapSet)
        parameter = Params(mapName, action, name, guid, guids, port)

        val urlResponse = urlCheck()
        if(urlResponse != null)
            return urlResponse

        val securityResponse = headerSecurityCheck(request)
        if(securityResponse != null)
            return securityResponse

        return entityAction()
    }

    private fun urlCheck(): ResponseEntity<Any>? {
        val validActions = arrayOf("join", "leave", "kick", "ban", "heartbeat")
        if(!validActions.contains(parameter.action))
            return ResponseEntity<Any>("400 - INVALID ACTION", HttpStatus.BAD_REQUEST)

        //Return if fields are empty, do not proceed with DB functions
        if(parameter.action != "heartbeat" && validActions.contains(parameter.action)
            && (parameter.name.isEmpty() || parameter.guid.isEmpty() || parameter.port == 0)) {
            return ResponseEntity<Any>("400 - BAD REQUEST", HttpStatus.BAD_REQUEST)
        } else if(parameter.action == "heartbeat") { //Heartbeat requires only the guids param
            if(parameter.guids == null)
                return ResponseEntity<Any>("400 - BAD REQUEST", HttpStatus.BAD_REQUEST)
        }
        return null
    }

    private fun headerSecurityCheck(request : HttpServletRequest): ResponseEntity<Any>? {
        //Security -> preventing bad clients from connecting
        if(properties.security.header_check.enabled) {
            val userAgent = request.getHeader("User-Agent")
            val requestHash = request.getHeader("x-secret-hash")
            val verifiedServer = properties.servers.filter { it.port == parameter.port && it.hash == requestHash }

            if (verifiedServer.isEmpty() || !userAgent.equals("Mw2-Server/1.0"))
                return ResponseEntity<Any>("401 - UNAUTHORIZED", HttpStatus.UNAUTHORIZED)
        }
        return null
    }

    private fun entityAction() : ResponseEntity<Any> {
        when (parameter.action) {
            "join", "leave", "kick", "ban", "unban" -> {
                //newPlayerEntity = the player entity we are going to modify/update
                val newPlayerEntity = modifyPlayerEntity.getPlayerEntity(parameter.name, parameter.guid)

                //Requests to update the existing player in database, or insert new
                try {
                    modifyPlayerEntity.updateEntity(newPlayerEntity, parameter.mapName, parameter.port, parameter.action)
                } catch (e: IllegalArgumentException) {
                    println(e.message) //Failed to update player in DB
                    return ResponseEntity<Any>("417 - FAILED TO UPDATE ENTITY", HttpStatus.EXPECTATION_FAILED)
                }
            }

            "heartbeat" -> {
                //newPlayerEntity = the player entity we are going to modify/update
                if (parameter.guids != null) {
                    val newPlayerEntities = modifyPlayerEntity.getPlayerEntities(parameter.guids!!)
                        ?: return ResponseEntity<Any>("204 - NO PLAYERS FOUND", HttpStatus.NO_CONTENT)

                    //Requests to update the existing player in database, or insert new
                    try {
                        modifyPlayerEntity.updateEntities(newPlayerEntities, parameter.mapName)
                    } catch (e: IllegalArgumentException) {
                        println(e.message) //Failed to update player in DB
                        return ResponseEntity<Any>("417 - FAILED TO UPDATE ENTITY", HttpStatus.EXPECTATION_FAILED)
                    }
                }
            }
        }
        return ResponseEntity<Any>("200 - OK", HttpStatus.OK)
    }

}