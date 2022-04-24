package iwkots.security

import iwkots.utils.yaml.ApiProperties
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
class SecurityConfig(private val authenticationProvider : CustomAuthenticationProvider,
                     private val properties : ApiProperties) : WebSecurityConfigurerAdapter() {

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
                //Disable CSRF (TEMP)
                .and().csrf().disable()
        } else {
            http.authorizeRequests()
                .antMatchers("/api/servers/*/*").hasRole("ADMIN")
                .antMatchers("/api/servers/list").hasRole("ADMIN")
                .anyRequest().authenticated()
                //Using Basic Auth (TEMP) - Updating to OAuth2, once the GSC code has been completed
                .and().httpBasic()
                //Disable CSRF (TEMP)
                .and().csrf().disable()
        }
    }

}