package iwkots.database.request

import iwkots.database.PlayerEntity
import iwkots.database.PlayerRepository
import iwkots.utils.Misc

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
class PlayerEntityOnMap(private val playerRepository : PlayerRepository) {

    //Returns list of players who has joined the map at any point in time
    fun listByMap(mapName : String) : List<PlayerEntity> {
        return playerRepository.getByLastMapName(mapName)
    }

    //Returns list of players who joined any map within the past minute
    fun listPlayersByRecentMapJoin(byMinute : Long) : List<PlayerEntity>  {
        val timestamp = Misc.getLocalDateAsTimestamp(byMinute)
        return playerRepository.getByLastSeenAfter(timestamp)
    }

    //Returns list of players who joined the map within the past minute
    fun listPlayersByRecentMapJoin(mapName : String, byMinute : Long) : List<PlayerEntity>  {
        val timestamp = Misc.getLocalDateAsTimestamp(byMinute)
        return playerRepository.getByLastSeenAfterAndLastMapName(timestamp, mapName)
    }

}