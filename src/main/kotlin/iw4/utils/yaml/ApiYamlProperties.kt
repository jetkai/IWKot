package iw4.utils.yaml

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("api")
data class ApiYamlProperties(val keys : List<ApiKeyProperties>)

data class ApiKeyProperties(val name : String, val key : String)