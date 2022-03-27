package dev.stillya.gitty.services.bot.telegram

import dev.stillya.gitty.dtos.BotMessage
import dev.stillya.gitty.services.bot.Bot
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class GitlabTelegramBot : TelegramWebhookBot(), Bot {

    @Value("\${telegram.bot.name}")
    private lateinit var botName: String

    @Value("\${telegram.bot.name}")
    private lateinit var botToken: String


    override fun sendMessage(message: BotMessage) {
        execute(SendMessage.builder().chatId(message.channel).text(message.text).build())
    }

    override fun onWebhookUpdateReceived(update: Update?): BotApiMethod<*> {
        TODO("Not yet implemented")
    }

    override fun getBotUsername(): String = botName

    override fun getBotToken(): String = botToken

    override fun getBotPath(): String {
        TODO("Not yet implemented")
    }
}