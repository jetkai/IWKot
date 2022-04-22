package iw4.security

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

    @Throws(Exception::class)
    override fun configure(authentication : AuthenticationManagerBuilder) {
        authentication.authenticationProvider(authenticationProvider)
    }

    @Throws(Exception::class)
    override fun configure(http : HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/api/servers/*/*").permitAll() //Allow anything beyond the API Dir
            .antMatchers("/api/servers/list").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and().csrf().disable()
       // http.formLogin().loginPage("/login").permitAll().and().logout().permitAll();
    }

}