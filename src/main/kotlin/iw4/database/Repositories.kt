package iw4.database

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * PlayerRepository
 *
 * Uses CrudRepository interface to run query's on the MariaDB
 *
 * @author Kai
 * @version 1.0, 19/04/2022
 */
@Repository("PlayerRepository")
interface PlayerRepository : CrudRepository<PlayerEntity, String> {

    fun getAllBy(): List<PlayerEntity?>?

    fun getByLastMapName(map : String) : List<PlayerEntity>

    fun getByLastMapNameAndLastSeenIsAfter(map : String, time : Date) : List<PlayerEntity>

    fun getByLastSeenAfter(time : Date) : List<PlayerEntity>

    fun getByNameAndGuid(name : String, guid : String) : PlayerEntity

/*    @Modifying
    @Transactional
    @Query("UPDATE PlayerEntity e set e.name = :name, e.guid = :guid, e.firstSeen = :firstSeen" +
            ", e.lastSeen = :lastSeen, e.connections = :connections, e.lastMapName = :lastMapName" +
            ", e.lastServerPort = :lastServerPort WHERE e.guid = :guid")
    fun updatePlayer(@Param("name") name : String, @Param("guid") guid : String, @Param("firstSeen") firstSeen : String,
                     @Param("lastSeen") lastSeen : String, @Param("connections") connections : String,
                     @Param("lastMapName") lastMapName : String, @Param("lastServerPort") lastServerPort : String)*/


}