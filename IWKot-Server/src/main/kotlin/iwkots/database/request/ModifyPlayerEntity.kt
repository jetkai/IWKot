package iwkots.database.request

import iwkots.database.PlayerEntity
import iwkots.database.PlayerRepository
import iwkots.utils.Misc
import org.springframework.dao.EmptyResultDataAccessException
import java.sql.SQLSyntaxErrorException

/**
 * ModifyPlayerEntity Class
 *
 * Communicates with MariaDB through CrudRepository interface
 * @see updateEntity
 * @see getPlayerEntity
 * @see getDefaultTemplate
 *
 * @author Kai
 * @version 1.0, 19/04/2022
 */
class ModifyPlayerEntity(private val playerRepository : PlayerRepository) {

    //Update single PlayerEntity
    fun updateEntity(player : PlayerEntity, mapName : String, port : Int, action : String) {
        //Attributes to modify
        when (action) {
            "join" -> player.connections = player.connections?.plus(1)
            "ban" -> player.isBanned = true
            "unban" -> player.isBanned = false
        }
        player.lastMapName = mapName
        player.lastServerPort = port
        player.lastSeen = Misc.getLocalDateAsTimestamp()
        //Writes to DB
        playerRepository.save(player)
    }

    //Updates PlayerEntity in bulk, single transaction
    fun updateEntities(players : MutableIterable<PlayerEntity>, mapName : String) {
        //Attributes to modify
        for(player in players) {
            player.lastSeen = Misc.getLocalDateAsTimestamp()
            player.lastMapName = mapName
        }
        //Writes to DB
        playerRepository.saveAll(players)
    }

    //Returns a single instance of PlayerEntity
    fun getPlayerEntity(name : String, guid : String) : PlayerEntity {
        return try { //Checks if exists in DB
            playerRepository.getByUsernameAndGuid(name, guid)
        } catch (e : EmptyResultDataAccessException) { //Returns the default template if player doesn't exist in DB
            println(e.message)
            getDefaultTemplate(name, guid)
        } catch (sql : SQLSyntaxErrorException) { //This is bad, should not get here!
            println(sql.message)
            getDefaultTemplate(name, guid)
        }
    }

    //Returns a list of PlayerEntity
    fun getPlayerEntities(guids : List<String>) : MutableIterable<PlayerEntity>? {
        return try { //Checks if exists in DB
            playerRepository.findByGuidIn(guids)?.toMutableSet()
        } catch (e : EmptyResultDataAccessException) { //Returns the default template if player doesn't exist in DB
            println(e.message)
            return null
        }
    }

    //Returns the default template of the PlayerEntity
    private fun getDefaultTemplate(name : String, guid : String) : PlayerEntity {
        val currentTime = Misc.getLocalDateAsTimestamp()

        val welcomeEntity = PlayerEntity()
        welcomeEntity.id = 0
        welcomeEntity.username = name
        welcomeEntity.guid = guid
        welcomeEntity.connections = 0
        welcomeEntity.firstSeen = currentTime
        welcomeEntity.lastSeen = currentTime
        welcomeEntity.lastMapName = "Default"
        welcomeEntity.lastServerPort = 28960
        return welcomeEntity
    }

}