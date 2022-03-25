package dev.stillya.gitty.services.bot.telegram

import org.springframework.beans.factory.annotation.Value
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

class GitlabTelegramBot(defaultBotOptions: DefaultBotOptions) : TelegramWebhookBot(defaultBotOptions) {

    @Value("\${telegram.bot.name}")
    private lateinit var botName: String

    @Value("\${telegram.bot.name}")
    private lateinit var botToken: String

    override fun getBotUsername(): String = botName

    override fun getBotToken(): String = botToken

    override fun onWebhookUpdateReceived(update: Update?): BotApiMethod<*> {
        TODO("Not yet implemented")
    }

    override fun getBotPath(): String {
        TODO("Not yet implemented")
    }
}