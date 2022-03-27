package dev.stillya.gitty.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "gitlab")
data class GitlabProperties(
    val baseUrl: String,
    val token: String
)