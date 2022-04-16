package iw4.controllers

import iw4.database.PlayerRepository
import iw4.database.request.ModifyPlayerEntity
import iw4.database.request.PlayerEntityOnMap
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Author: Kai
 */
@RestController
class WelcomePlayer(var playerRepository : PlayerRepository) {

    @RequestMapping(value = ["/api/servers/{mapName}/join"], produces = ["text/plain; charset=utf-8"], method = [RequestMethod.POST])
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
            return ResponseEntity<Any>("417 - FAILED TO SAVE PLAYER", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity<Any>("200 - OK", HttpStatus.OK);
    }

    @RequestMapping(value = ["/api/servers/{mapName}/leave"], produces = ["text/plain; charset=utf-8"], method = [RequestMethod.POST])
    fun onPlayerLeave(@PathVariable mapName : String) {
        //TODO
    }

    @RequestMapping(value = ["/api/servers/listPlayers"], produces = ["text/plain; charset=utf-8"], method = [RequestMethod.GET])
    fun onServerListPlayers(@RequestParam(value = "recent", required = false, defaultValue = "false") isRecent : Boolean): ResponseEntity<Any> {
        val playerEntityOnMap = PlayerEntityOnMap(playerRepository) //TODO - Maybe make this local to class

        //Returns a list of players who have connected within the past minute
        if(isRecent) {
            val players = playerEntityOnMap.listPlayersByRecentMapJoin()
            return ResponseEntity<Any>(players, HttpStatus.OK);
        }

        return ResponseEntity<Any>("200 - OK", HttpStatus.OK);
    }

}