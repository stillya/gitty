package dev.stillya.gitty.services.handlers

import dev.stillya.gitty.dtos.types.EventType

interface EventHandler<T> {
    abstract val type: EventType
    fun handle(event: T)
}