package iw4.database.request

import iw4.database.PlayerEntity
import iw4.database.PlayerRepository
import iw4.utils.Misc

/**
 * Author: Kai
 */
class PlayerEntityOnMap(private var playerRepository : PlayerRepository) {

    fun listByMap(mapName : String) : List<PlayerEntity> {
        return playerRepository.getByLastMapName(mapName);
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