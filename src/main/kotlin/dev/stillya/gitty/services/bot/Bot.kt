package dev.stillya.gitty.services.bot

import dev.stillya.gitty.dtos.BotMessage

interface Bot {
    fun sendMessage(message: BotMessage)
}