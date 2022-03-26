package dev.stillya.gitty.services.bot

import dev.stillya.gitty.services.bot.types.EventType

interface EventHandler<T> {
    abstract val type: EventType
    fun handle(event: T)
}