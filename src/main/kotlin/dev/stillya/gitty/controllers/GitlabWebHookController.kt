package dev.stillya.gitty.controllers

import dev.stillya.gitty.services.handlers.EventHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/gitlab"])
class GitlabWebHookController(private val handlers: Collection<EventHandler<*>>) {
}