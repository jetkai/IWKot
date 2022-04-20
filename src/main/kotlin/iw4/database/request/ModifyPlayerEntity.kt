package iw4.database.request

import iw4.database.PlayerEntity
import iw4.database.PlayerRepository
import iw4.utils.Misc
import org.springframework.dao.EmptyResultDataAccessException

/**
 * ModifyPlayerEntity Class
 *
 * Communicates with MariaDB through CrudRepository interface
 * @see update
 * @see getPlayerEntity
 * @see getDefaultTemplate
 *
 * @author Kai
 * @version 1.0, 19/04/2022
 */
class ModifyPlayerEntity(private var playerRepository : PlayerRepository) {

    fun update(player : PlayerEntity, mapName : String, port : Int) {

        //Attributes to modify
        player.lastSeen = Misc.getLocalDateAsTimestamp()
        player.connections = player.connections?.plus(1)
        player.lastMapName = mapName
        player.lastServerPort = port

        //Writes to DB
        playerRepository.save(player)
    }

    //Returns the PlayerEntity
    fun getPlayerEntity(name : String, guid : String) : PlayerEntity {
        return try { //Checks if exists in DB
            playerRepository.getByNameAndGuid(name, guid)
        } catch (e: EmptyResultDataAccessException) { //Returns the default template if player doesn't exist in DB
            getDefaultTemplate(name, guid)
        }
    }

    private fun getDefaultTemplate(name : String, guid : String) : PlayerEntity {
        val currentTime = Misc.getLocalDateAsTimestamp()

        val welcomeEntity = PlayerEntity()

        welcomeEntity.name = name
        welcomeEntity.guid = guid
        welcomeEntity.connections = 0
        welcomeEntity.firstSeen = currentTime
        welcomeEntity.lastSeen = currentTime
        welcomeEntity.lastMapName = "Default"
        welcomeEntity.lastServerPort = 28960

        return welcomeEntity
    }

}