package iw4.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        // authentication manager (see below)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
            //.antMatchers("/admin/**").hasRole("ADMIN")
            //.antMatchers("/anonymous*").anonymous()
            .antMatchers("/api/servers/*/*").permitAll() //Allow anything beyond the API Dir
            .antMatchers("/api/servers/list").permitAll()
            .anyRequest().authenticated()
            .and()
    }

}