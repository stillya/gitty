package dev.stillya.gitty.services.handlers

import dev.stillya.gitty.dtos.BotMessage
import dev.stillya.gitty.dtos.MergeRequestEvent
import dev.stillya.gitty.dtos.types.EventType
import dev.stillya.gitty.repositories.TelegramUserRepository
import dev.stillya.gitty.services.bot.telegram.GitlabTelegramBot
import org.springframework.stereotype.Component

@Component
class MergeEventHandler(override val type: EventType = EventType.MERGE_REQUEST, val bot: GitlabTelegramBot, val repository: TelegramUserRepository) :
    EventHandler<MergeRequestEvent> {
    override suspend fun handle(event: MergeRequestEvent) {
        if (event.mergeRequest!!.status == "merged") {
            repository.getUserByUsername(event.user!!.username!!)?.let {
                if (it.eventTypes!!.contains("merge")) {
                    bot.sendMessage(BotMessage(createMessage(event), it.chatId!!))
                }
            }
        }
    }

    private fun createMessage(event: MergeRequestEvent): String {
        return "⚡️ Your merge request ${event.mergeRequest!!.title} has been ${event.mergeRequest.status} by ${event.user!!.username} ⚡️\n\n" +
                "Link: ${event.mergeRequest.url}" + "\n" +
                "Source branch: ${event.mergeRequest.sourceBranch}\n" +
                "Target branch: ${event.mergeRequest.targetBranch}\n"
    }
}