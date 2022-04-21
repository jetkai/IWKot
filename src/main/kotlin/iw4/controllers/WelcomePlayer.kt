package iw4.controllers

import iw4.database.PlayerRepository
import iw4.database.request.ModifyPlayerEntity
import iw4.database.request.PlayerEntityOnMap
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * WelcomePlayer Class
 *
 * Used as a RestController for API Queries
 * @see onPlayerJoin
 * @see onPlayerLeave
 * @see onServerListPlayers
 *
 * @author Kai
 * @version 1.0, 19/04/2022
 */
@RestController
class WelcomePlayer(var playerRepository : PlayerRepository) {

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
    @RequestMapping(value = ["/api/servers/{mapName}/join"], produces = ["text/plain; charset=utf-8"], method = [RequestMethod.GET])
    fun onPlayerJoin(@PathVariable mapName : String,
                     @RequestParam(value = "name", required = true, defaultValue = "") name : String,
                     @RequestParam(value = "guid", required = true, defaultValue = "") guid : String,
                     @RequestParam(value = "port", required = true, defaultValue = "") port : String) : ResponseEntity<Any> {

        //Return if fields are empty, do not proceed with DB functions
        if(name.isEmpty() || guid.isEmpty() || port.isEmpty())
            return ResponseEntity<Any>("400 - BAD REQUEST", HttpStatus.BAD_REQUEST)

        //Checks if player exists in database to modify existing data, otherwise creates them
        val modifyPlayerEntity = ModifyPlayerEntity(playerRepository) //TODO - Maybe make this local to class

        //newPlayerEntity = the player entity we are going to modify/update
        val newPlayerEntity = modifyPlayerEntity.getPlayerEntity(name, guid)

        //Requests to update the existing player in database, or insert new
        try {
            modifyPlayerEntity.update(newPlayerEntity, mapName, port.toInt())
        } catch (e : IllegalArgumentException) {
            println(e.message) //Failed to update player in DB
            return ResponseEntity<Any>("417 - FAILED TO SAVE PLAYER", HttpStatus.EXPECTATION_FAILED)
        }

        return ResponseEntity<Any>("200 - OK", HttpStatus.OK)
    }

    /**
     * This function will modify/update the Player within the SQL Database
     * @since version 1.0
     *
     * @param mapName The name of the map that requires to be updated
     *
     * @return 200 - OK (in plaintext)
     * @throws 417 - FAILED TO SAVE PLAYER (in plaintext)
     */
    @RequestMapping(value = ["/api/servers/{mapName}/leave"], produces = ["text/plain; charset=utf-8"], method = [RequestMethod.POST])
    fun onPlayerLeave(@PathVariable mapName : String) {
        //TODO
    }

    /**
     * This function will list players on the server/map
     * (GSC can only parse CSV/Plain-Text)
     * @since version 1.0
     *
     * @param isRecent Returns players who were on the server within the last minute
     * @return List of players (as CSV)
     */
    @RequestMapping(value = ["/api/servers/listPlayers"], produces = ["text/plain; charset=utf-8"], method = [RequestMethod.GET])
    fun onServerListPlayers(@RequestParam(value = "recent", required = false, defaultValue = "false") isRecent : Boolean): ResponseEntity<Any> {
        val playerEntityOnMap = PlayerEntityOnMap(playerRepository) //TODO - Maybe make this local to class

        //Returns a list of players who have connected within the past minute
        if(isRecent) {
            val players = playerEntityOnMap.listPlayersByRecentMapJoin()
            return ResponseEntity<Any>(players, HttpStatus.OK)
        }

        return ResponseEntity<Any>("200 - OK", HttpStatus.OK)
    }

}