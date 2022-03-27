package dev.stillya.gitty.services.handlers

import dev.stillya.gitty.dtos.MergeRequestEvent
import dev.stillya.gitty.dtos.types.EventType
import org.springframework.stereotype.Component

@Component
class MergeEventHandler(override val type: EventType = EventType.MERGE_REQUEST) : EventHandler<MergeRequestEvent> {
    override fun handle(event: MergeRequestEvent) {
        println("Merge request event: $event")
    }
}