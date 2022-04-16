package iw4.database

import java.sql.Timestamp
import javax.persistence.*

/**
 * Author: Kai
 */
@Entity
@Table(name = "mw2_welcome", schema = "localdb")
class PlayerEntity {

    @Id
    @Column(name = "id", nullable = false)
    var id: Int? = null

    @Basic
    @Column(name = "name", nullable = false)
    var name: String? = null

    @Basic
    @Column(name = "guid", nullable = false)
    var guid: String? = null

    @Basic
    @Column(name = "firstSeen", nullable = false)
    var firstSeen: Timestamp? = null

    @Basic
    @Column(name = "lastSeen", nullable = false)
    var lastSeen: Timestamp? = null

    @Basic
    @Column(name = "connections", nullable = false)
    var connections: Int? = null

    @Basic
    @Column(name = "lastMapName", nullable = false)
    var lastMapName: String? = null

    @Basic
    @Column(name = "lastServerPort", nullable = false)
    var lastServerPort: Int? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "name = $name " +
                "guid = $guid " +
                "firstSeen = $firstSeen " +
                "lastSeen = $lastSeen " +
                "connections = $connections " +
                "lastMapName = $lastMapName " +
                "lastServerPort = $lastServerPort " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PlayerEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (guid != other.guid) return false
        if (firstSeen != other.firstSeen) return false
        if (lastSeen != other.lastSeen) return false
        if (connections != other.connections) return false
        if (lastMapName != other.lastMapName) return false
        if (lastServerPort != other.lastServerPort) return false

        return true
    }
}