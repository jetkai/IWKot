package iw4.security

import iw4.utils.yaml.ApiProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

/**
 * SecurityConfig
 *
 * Allows access to mapped directories, specified in the below functions
 * @see configure
 *
 * @author Kai
 * @version 1.0, 19/04/2022
 */
@Configuration
@EnableWebSecurity/*(debug = true)*/
class SecurityConfig(val authenticationProvider : CustomAuthenticationProvider) : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var properties : ApiProperties

    @Throws(Exception::class)
    override fun configure(authentication : AuthenticationManagerBuilder) {
        authentication.authenticationProvider(authenticationProvider)
    }

    @Throws(Exception::class)
    override fun configure(http : HttpSecurity) {
        if(properties.security.public_access.enabled) {
            http.authorizeRequests()
                .antMatchers("/api/servers/*/*").permitAll()
                .antMatchers("/api/servers/list").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
        } else {
            http.authorizeRequests()
                .antMatchers("/api/servers/*/*").hasRole("ADMIN")
                .antMatchers("/api/servers/list").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().httpBasic() //Using Basic Auth (TEMP) - Updating to OAuth2, once the GSC code has been completed
                .and().csrf().disable()
        }
    }

}