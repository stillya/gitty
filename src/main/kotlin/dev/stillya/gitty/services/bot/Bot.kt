package dev.stillya.gitty.services.bot

import dev.stillya.gitty.services.bot.dto.BotMessage

interface Bot {
    fun sendMessage(message: BotMessage)
}