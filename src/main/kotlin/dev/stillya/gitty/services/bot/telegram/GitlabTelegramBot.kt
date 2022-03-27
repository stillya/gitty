package dev.stillya.gitty.services.bot.telegram

import com.github.kotlintelegrambot.entities.ChatId
import dev.stillya.gitty.dtos.BotMessage
import dev.stillya.gitty.services.bot.Bot
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class GitlabTelegramBot(private val bot: com.github.kotlintelegrambot.Bot) : Bot {

    companion object : KLogging()

    override fun sendMessage(message: BotMessage) {
        val res = bot.sendMessage(ChatId.fromId(message.channel.toLong()), message.text)
        logger.info { "Message sent: ${res.first} ${res.second}" }
    }
}