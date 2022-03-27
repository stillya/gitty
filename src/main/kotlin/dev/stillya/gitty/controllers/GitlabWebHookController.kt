package dev.stillya.gitty.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import dev.stillya.gitty.dtos.MergeRequestEvent
import dev.stillya.gitty.services.handlers.EventHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/gitlab"])
class GitlabWebHookController(private val handlers: Collection<EventHandler<*>>) {

    @PostMapping(path = ["/merge"])
    fun webhook(@RequestBody update: MergeRequestEvent) {
        println("Received webhook: $update")
    }
}