package iw4.controllers.response

import iw4.database.PlayerRepository
import iw4.database.request.PlayerEntityOnMap
import iw4.utils.yaml.ApiProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

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
class PlayerList(playerRepository : PlayerRepository, val properties : ApiProperties) {

    private final val playerEntityOnMap = PlayerEntityOnMap(playerRepository)

    /**
     * This function will list players on the server/map
     * (GSC can only parse CSV/Plain-Text)
     * @since version 1.0
     *
     * @param recent Returns players who were on the server within the last <*> minutes
     * @return List of players (as CSV)
     */
    @RequestMapping(value = ["/api/servers/list"], produces = ["application/json"], method = [RequestMethod.GET])
    fun onServerListPlayers(@RequestParam(value = "recent", required = true, defaultValue = "0") recent : Long,
                            @RequestParam(value = "mapName", required = false) mapName : String,
                            request : HttpServletRequest) : ResponseEntity<Any> {

        val urlResponse = urlCheck(recent, mapName)
        if(urlResponse != null)
            return urlResponse

        val securityResponse = headerSecurityCheck(request)
        if(securityResponse != null)
            return securityResponse

        return entityResponse(recent, mapName)
    }

    private fun urlCheck(request : Long, mapName : String) : ResponseEntity<Any>? {
        if(request <= 0L)
            return ResponseEntity<Any>(null, HttpStatus.NO_CONTENT)

        val validMapName = properties.servers.map { it.name }
        if(mapName.isNotEmpty() && !validMapName.contains(mapName))
            return ResponseEntity<Any>("400 - INVALID MAP", HttpStatus.BAD_REQUEST)
        return null
    }

    private fun headerSecurityCheck(request : HttpServletRequest) : ResponseEntity<Any>? {
        //Security -> preventing bad clients from connecting
        if(properties.security.header_check.enabled) {
            val userAgent = request.getHeader("User-Agent")
            if (!userAgent.equals("Mw2-Server/1.0"))
                return ResponseEntity<Any>("401 - UNAUTHORIZED", HttpStatus.UNAUTHORIZED)
        }
        return null
    }

    private fun entityResponse(recent : Long, mapName: String) : ResponseEntity<Any> {
        //Returns a list of players who have connected within the past @param recent minutes
        val players = if(mapName.isNotEmpty()) { //All Players on Specific Map
            playerEntityOnMap.listPlayersByRecentMapJoin(mapName, recent)
        } else { //All Players on All Maps
            playerEntityOnMap.listPlayersByRecentMapJoin(recent)
        }
        return ResponseEntity<Any>(players, HttpStatus.OK)
    }

}