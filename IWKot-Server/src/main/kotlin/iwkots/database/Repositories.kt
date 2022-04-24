package iwkots.database

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * PlayerRepository
 *
 * Uses CrudRepository interface to run query's on the MariaDB
 *
 * Great ref for findBy/getBy usage (24/04/2022):
 * https://szymonkrajewski.pl/the-practical-difference-between-findby-and-getby-in-repositories/
 *
 * @author Kai
 * @version 1.0, 19/04/2022
 */
@Repository("PlayerRepository")
interface PlayerRepository : CrudRepository<PlayerEntity, String> {

    fun getAllBy(): List<PlayerEntity?>?

    fun getByLastMapName(map : String) : List<PlayerEntity>

    fun getByLastSeenAfter(time : Date) : List<PlayerEntity>

    //fun getByLastMapNameAndLastSeenAfter(mapName : String, time : Date) : List<PlayerEntity>
    fun getByLastSeenAfterAndLastMapName(time : Date, mapName : String) : List<PlayerEntity> //TODO - Test Query Speed

    fun getByUsernameAndGuid(name : String, guid : String) : PlayerEntity

    fun findByGuidIn(guid : List<String>) : List<PlayerEntity>?

/*    @Modifying
    @Transactional
    @Query("UPDATE PlayerEntity e set e.name = :name, e.guid = :guid, e.firstSeen = :firstSeen" +
            ", e.lastSeen = :lastSeen, e.connections = :connections, e.lastMapName = :lastMapName" +
            ", e.lastServerPort = :lastServerPort WHERE e.guid = :guid")
    fun updatePlayer(@Param("name") name : String, @Param("guid") guid : String, @Param("firstSeen") firstSeen : String,
                     @Param("lastSeen") lastSeen : String, @Param("connections") connections : String,
                     @Param("lastMapName") lastMapName : String, @Param("lastServerPort") lastServerPort : String)*/


}