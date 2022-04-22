package iw4

import iw4.database.PlayerEntity
import iw4.utils.Mw2HttpClient
import iw4.utils.yaml.ApiProperties
import iw4.utils.yaml.ServerProperties
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

/**
 * CreatePlayerTest
 *
 * Test class for calling the mapped function which registers player creation
 *
 * @author Kai
 * @version 1.0, 20/04/2022
 */
@SpringBootTest(classes = [Mw2ServerKtApplication::class]
/*, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT*/)
class CreatePlayerTest {

    @Autowired
    lateinit var properties : ApiProperties

   /* //Resources path
    private val resourcePath = this::class.java.getResource("")?.path
        ?.replace("/classes/kotlin/test/iw4/", "/resources/main/")
        ?.substring(1) //Replaces initial "/" char

    //Unique key for the WelcomePlayer Controller (BCrypt)
    private val apiKey = Path.of(resourcePath + "keys/WelcomePlayer.key").readText()*/

    @Test
    fun contextLoads() {
        val server = properties.servers.first() //{ it.port == player.lastServerPort }

        val player = buildTestEntity()
        val destination = buildTestUrl(server, player)

        val mw2HttpClient = Mw2HttpClient(destination, server.hash)
        val response = mw2HttpClient.requestBody()

        assert(response == "200 - OK")
    }

    fun buildTestEntity() : PlayerEntity {
        val player = PlayerEntity()
        player.username = "Kai Test"
        player.guid = "kaitestguid123456"
        player.lastServerPort = 28960
        player.lastMapName = "oilrig"
        return player
    }

    //Example Final: http://localhost:28600/api/servers/oilrig/join?name=Kai%20Test&guid=kaitestguid123456&port=28960
    fun buildTestUrl(server : ServerProperties, player : PlayerEntity): URI {
        return UriComponentsBuilder.newInstance()
            .scheme("http") //HTTP for local testing
            .host("localhost").port(28600)
            .path("api/servers")
            .path("/").path(player.lastMapName!!)
            .path("/").path("join")
            .queryParam("name", player.username)
            .queryParam("guid", player.guid)
            .queryParam("port", player.lastServerPort)
            .build().toUri()
    }

}
