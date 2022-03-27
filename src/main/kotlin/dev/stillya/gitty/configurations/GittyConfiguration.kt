package dev.stillya.gitty.configurations

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GittyConfiguration {

    @Value("\${telegram.bot.name}")
    private lateinit var botName: String

    @Value("\${telegram.bot.token}")
    private lateinit var botToken: String

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()

    @Bean
    fun bot(): Bot {
        val bot = bot {
            token = botToken
        }
        return bot
    }
}