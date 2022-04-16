package iw4.controllers

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class WelcomePlayer {

    @RequestMapping(value = ["/api/servers/{mapName}/join"], produces = ["application/json"], method = [RequestMethod.POST])
    fun onPlayerJoin(@PathVariable mapName : String) {

    }

    @RequestMapping(value = ["/api/servers/{mapName}/leave"], produces = ["application/json"], method = [RequestMethod.POST])
    fun onPlayerLeave(@PathVariable mapName : String) {

    }

    @RequestMapping(value = ["/api/servers/listPlayers"], produces = ["application/json"], method = [RequestMethod.GET])
    fun onServerListPlayers() {

    }

}