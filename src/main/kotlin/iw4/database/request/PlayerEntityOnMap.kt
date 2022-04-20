package iw4.database.request

import iw4.database.PlayerEntity
import iw4.database.PlayerRepository
import iw4.utils.Misc

/**
 * PlayerEntityOnMap
 *
 * Class that contains functions which you can return as List<PlayerEntity>
 * @see listByMap
 * @see listPlayersByRecentMapJoin
 * @see listByMapDefault
 *
 * @author Kai
 * @version 1.0, 19/04/2022
 */
class PlayerEntityOnMap(private var playerRepository : PlayerRepository) {

    fun listByMap(mapName : String) : List<PlayerEntity> {
        return playerRepository.getByLastMapName(mapName)
    }

    //Returns list of players who joined the map within the past minute
    fun listPlayersByRecentMapJoin() : List<PlayerEntity>  {
        val timestamp = Misc.getLocalDateAsTimestamp()
        return playerRepository.getByLastSeenAfter(timestamp)
    }

    //Returns list of players who joined the map within the past minute
    fun listByMapDefault(mapName: String) : List<PlayerEntity> {
        val timestamp = Misc.getLocalDateAsTimestamp()
        return playerRepository.getByLastMapNameAndLastSeenIsAfter(mapName, timestamp)
    }

}