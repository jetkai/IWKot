package iw4.utils.yaml

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("config")
//@PropertySource(value = ["classpath:config.yml"])
data class ConfigYamlProperties(val name : String, val ips : List<String>)