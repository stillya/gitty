package dev.stillya.gitty.services.bot

import dev.stillya.gitty.services.bot.dto.BotEvent
import dev.stillya.gitty.services.bot.types.EventType

interface EventHandler {
    abstract val type: EventType
    fun handle(event: BotEvent)
}