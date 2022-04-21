package iw4

import iw4.utils.yaml.ApiYamlProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


/**
 * Mw2ServerKtApplication
 *
 * Main class for the Spring Boot Application
 *
 * @author Kai
 * @version 1.0, 19/04/2022
 */
//@Configuration
//@EnableConfigurationProperties
//@ConfigurationPropertiesScan
@SpringBootApplication
@EnableConfigurationProperties(ApiYamlProperties::class)
class Mw2ServerKtApplication

fun main(args: Array<String>) {
    runApplication<Mw2ServerKtApplication>(*args)
}
