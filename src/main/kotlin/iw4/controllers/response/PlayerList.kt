package iw4.controllers.response

import iw4.database.PlayerRepository
import iw4.database.request.PlayerEntityOnMap
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * PlayerList Class
 *
 * Used as a RestController for API Queries
 * @see onServerListPlayers
 *
 * @author Kai
 * @version 1.0, 24/04/2022
 */
@RestController
class PlayerList(private val playerRepository : PlayerRepository) {

    /**
     * This function will list players on the server/map
     * (GSC can only parse CSV/Plain-Text)
     * @since version 1.0
     *
     * @param recent Returns players who were on the server within the last <*> minutes
     * @return List of players (as CSV)
     */
    @RequestMapping(value = ["/api/servers/list"], produces = ["application/json"], method = [RequestMethod.GET])
    fun onServerListPlayers(@RequestParam(value = "recent", required = false, defaultValue = "0") recent : Long) : ResponseEntity<Any> {

        val playerEntityOnMap = PlayerEntityOnMap(playerRepository)

        //Returns a list of players who have connected within the past @param recent minutes
        if(recent > 0) {
            val players = playerEntityOnMap.listPlayersByRecentMapJoin(recent)
            return ResponseEntity<Any>(players, HttpStatus.OK)
        }

        return ResponseEntity<Any>("200 - OK", HttpStatus.OK)
    }

}