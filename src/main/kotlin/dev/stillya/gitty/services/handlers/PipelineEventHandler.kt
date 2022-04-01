package dev.stillya.gitty.services.handlers

import dev.stillya.gitty.dtos.BotMessage
import dev.stillya.gitty.dtos.MergeRequestDto
import dev.stillya.gitty.dtos.PipelineEvent
import dev.stillya.gitty.dtos.types.EventType
import dev.stillya.gitty.repositories.TelegramUserRepository
import dev.stillya.gitty.services.bot.telegram.GitlabTelegramBot
import dev.stillya.gitty.services.git.GitClient
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class PipelineEventHandler(
    override val type: EventType = EventType.PIPELINE,
    val bot: GitlabTelegramBot,
    val repository: TelegramUserRepository,
    val gitClient: GitClient
) :
    EventHandler<PipelineEvent> {

    companion object : KLogging()

    override suspend fun handle(event: PipelineEvent) {
        if (event.status == "success" || event.status == "failed") {
            val mergeRequest = gitClient.getMergeRequest(event.pipelineId!!, event.projectId!!)

            if (mergeRequest.isFailure) {
                logger.error { "Failed to get merge request for event: $event" }
            } else {
                repository.getUserByUsername(mergeRequest.getOrThrow().author!!.username!!)?.let {
                    if (it.eventTypes!!.contains(EventType.PIPELINE.value)) {
                        bot.sendMessage(BotMessage(createMessage(event, mergeRequest.getOrThrow()), it.chatId!!))
                    }
                }
            }

        }
    }

    private fun createMessage(event: PipelineEvent, mr: MergeRequestDto): String {
        return "${statusToEmoji(event.status!!)} Your pipeline has been finish with ${event.status} ️\n\n" +
                "Link: ${event.url}" + "\n" +
                "Source branch: ${mr.sourceBranch}\n" +
                "Target branch: ${mr.targetBranch}\n"
    }

    private fun statusToEmoji(status: String): String {
        return when (status) {
            "success" -> "✅"
            "failed" -> "❌"
            else -> "⚠️"
        }
    }
}