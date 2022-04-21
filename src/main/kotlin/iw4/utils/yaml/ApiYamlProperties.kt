package iw4.utils.yaml

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("api")
data class ApiYamlProperties(val servers : List<ServerProperties>, val debug : Boolean)

data class ServerProperties(val name : String, val ip : String, val port : Int, val key : String)