package iw4

import iw4.utils.yaml.ApiYamlProperties
import iw4.utils.yaml.ConfigYamlProperties
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Mw2ServerKtApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreatePlayerTest {

    @Autowired
    lateinit var configYamlProperties : ConfigYamlProperties

    @Autowired
    lateinit var apiKeyProperties : ApiYamlProperties

   /* //Resources path
    private val resourcePath = this::class.java.getResource("")?.path
        ?.replace("/classes/kotlin/test/iw4/", "/resources/main/")
        ?.substring(1) //Replaces initial "/" char

    //Unique key for the WelcomePlayer Controller (BCrypt)
    private val apiKey = Path.of(resourcePath + "keys/WelcomePlayer.key").readText()*/

    @Test
    fun contextLoads() {

        println(configYamlProperties.ips)
        println(apiKeyProperties.keys)
       // val mw2HttpClient = Mw2HttpClient(destinations, apiKey)
    }

}
