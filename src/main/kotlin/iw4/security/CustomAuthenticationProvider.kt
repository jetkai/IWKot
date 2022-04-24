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
class CustomAuthenticationProvider(private val properties : ApiProperties) : AuthenticationProvider {

    //Stores a list of allowedIps that can connect
    private val allowedIps = mutableListOf<String>()
    private val publicAccess : Boolean = properties.security.public_access.enabled

    //Populates the allowedIps list upon class initialization
    init {
        if(!publicAccess) {
            //Localhost
            val allowList = mutableListOf("0:0:0:0:0:0:0:1", "0.0.0.0", "127.0.0.1", "localhost")
            //Game Servers
            val serverIps = properties.servers.map { it.ip }
            allowList.addAll(serverIps)
            //Adds the distinct ips from the allowList to the allowedIps val
            this.allowedIps.addAll(allowList.distinct())
        }
    }

    //Verifies if the remote ip is on the allowedIps list, otherwise throws BadCredentialsException
    //Using Basic Auth (TEMP) - Updating to OAuth2, once the GSC code has been completed
    @Throws(AuthenticationException::class)
    override fun authenticate(authentication : Authentication) : Authentication {
        //Gathers username/password from Basic Auth request
        val username : String = authentication.name
        val password : String = authentication.credentials.toString()
        val details = authentication.details as WebAuthenticationDetails
        //clientId/secret is just for Basic Auth currently, not OAuth2
        val clientId = properties.temp_client_id
        val clientSecret = properties.temp_client_secret

        val ip = details.remoteAddress
        //Refuses connection if IP is not whitelisted & publicAccess is disabled
        if (!allowedIps.contains(ip) && !publicAccess)
            throw BadCredentialsException("Connection refused.")

        val isCredentialsValid = (username == clientId && BCrypt.checkpw(password, clientSecret))
        //Checks if credentials are valid before granting role
        if(isCredentialsValid) {
            val authorities : MutableList<GrantedAuthority> = mutableListOf(SimpleGrantedAuthority("ROLE_ADMIN"))
            return UsernamePasswordAuthenticationToken(clientId, clientSecret, authorities)
        }

        throw BadCredentialsException("Invalid clientId or clientSecret.")
    }

    //TODO
    override fun supports(authentication : Class<*>) : Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

}