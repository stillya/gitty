package dev.stillya.gitty.configurations

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GittyConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
}