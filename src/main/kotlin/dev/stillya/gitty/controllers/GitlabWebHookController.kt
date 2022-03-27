package dev.stillya.gitty.controllers

import dev.stillya.gitty.dtos.MergeRequestEvent
import dev.stillya.gitty.dtos.PipelineEvent
import dev.stillya.gitty.services.handlers.EventHandler
import mu.KLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/gitlab"])
class GitlabWebHookController(private val handlers: Collection<EventHandler<*>>) {

    companion object : KLogging()

    @PostMapping(path = ["/merge"])
    suspend fun merge(@RequestBody update: MergeRequestEvent) {
        logger.info { "Received webhook merge: ${update.mergeRequest!!.status} from ${update.mergeRequest.url}" }
        handlers.find { it.type.value == "merge" }?.let {
            val h = it as EventHandler<MergeRequestEvent>
            h.handle(update)
        }
    }

    @PostMapping(path = ["/pipeline"])
    suspend fun pipeline(@RequestBody update: PipelineEvent) {
        logger.info { "Received webhook pipeline: ${update.status} from ${update.url}" }
        handlers.find { it.type.value == "pipeline" }?.let {
            val h = it as EventHandler<PipelineEvent>
            h.handle(update)
        }
    }
}