package dev.stillya.gitty.controllers

import dev.stillya.gitty.services.bot.telegram.TelegramMessageHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@RestController
@RequestMapping(path = ["/telegram"])
class TelegramWebHookController(private val telegramMessageHandler: TelegramMessageHandler) {

    @PostMapping(path = ["/webhook"])
    suspend fun webhook(@RequestBody update: Update): BotApiMethod<*> {
        val message = update.message
        val result = telegramMessageHandler.handle(message.chatId.toString(), message.text)
        return SendMessage(result.channel, result.text)
    }

    @PostMapping(path = ["/test"])
    suspend fun test(): String {
        return "test"
    }
}