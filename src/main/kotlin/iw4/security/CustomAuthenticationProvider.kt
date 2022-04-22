package iw4.security

import iw4.utils.yaml.ApiProperties
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component

/**
 * CustomAuthenticationProvider
 *
 * Protection against non-authorized networks/devices
 * @see authenticate
 *
 * @author Kai
 * @version 1.0, 21/04/2022
 */
@Component
class CustomAuthenticationProvider(private final val properties : ApiProperties) : AuthenticationProvider {

    //Stores a list of allowedIps that can connect
    private var allowedIps = mutableListOf<String>()
    private val publicAccess : Boolean = properties.security.public_access.enabled

    //Populates the allowedIps list upon class initialization
    init {
        if(!publicAccess) {
            //Localhost
            val localIps = arrayOf("0.0.0.0", "127.0.0.1", "localhost")
            this.allowedIps.addAll(localIps)

            //Game Servers
            val serverIps = properties.servers.map { it.ip }
            this.allowedIps.addAll(serverIps)

            //Removes duplicates
            this.allowedIps = this.allowedIps.distinct().toMutableList()

            println("Total Ips: ${this.allowedIps.size}")
        }
    }

    //Verifies if the remote ip is on the allowedIps list, otherwise throws BadCredentialsException
    @Throws(AuthenticationException::class)
    override fun authenticate(authentication : Authentication) : Authentication {
        val username : String = authentication.name
        val password : String = authentication.credentials.toString()

        val clientId = properties.temp_client_id
        val clientSecret = properties.temp_client_secret

        println("Allowed Ips: $allowedIps")

        val details = authentication.details as WebAuthenticationDetails
        val ip = details.remoteAddress

        if (!allowedIps.contains(ip) && !publicAccess)
            throw BadCredentialsException("Connection refused.")

        if(username == clientId && BCrypt.checkpw(clientSecret, password)) {
            val authorities : MutableList<GrantedAuthority> = ArrayList()
            authorities.add(SimpleGrantedAuthority("ROLE_ADMIN"))
            return UsernamePasswordAuthenticationToken(clientId, clientSecret, authorities)
        }
        throw BadCredentialsException("Invalid client / secret.")
    }

    //TODO
    override fun supports(authentication : Class<*>) : Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

}