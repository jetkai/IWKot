package iwkots.utils.yaml

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("api")
//Api (Root)
data class ApiProperties(val temp_client_id : String, val temp_client_secret : String,
                         val servers : List<ServerProperties>, val security : SecurityProperties)

//Server (Sub)
data class ServerProperties(val name : String, val ip : String, val port : Int, val hash : String)

//Security (Parent)
data class SecurityProperties(val header_check : HeaderCheckProperties, val public_access : PublicAccessProperties)
//Security (Child)
data class HeaderCheckProperties(val enabled : Boolean)
data class PublicAccessProperties(val enabled : Boolean)