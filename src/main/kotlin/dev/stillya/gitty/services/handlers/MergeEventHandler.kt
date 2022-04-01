package dev.stillya.gitty.services.handlers

import dev.stillya.gitty.dtos.BotMessage
import dev.stillya.gitty.dtos.MergeRequestEvent
import dev.stillya.gitty.dtos.types.EventType
import dev.stillya.gitty.repositories.TelegramUserRepository
import dev.stillya.gitty.services.bot.telegram.GitlabTelegramBot
import dev.stillya.gitty.services.git.GitClient
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class MergeEventHandler(
    override val type: EventType = EventType.MERGE_REQUEST,
    val bot: GitlabTelegramBot,
    val repository: TelegramUserRepository,
    val client: GitClient
) :
    EventHandler<MergeRequestEvent> {

    companion object : KLogging()

    override suspend fun handle(event: MergeRequestEvent) {
        if (event.mergeRequest!!.status == "merged") {
            val user = client.getUserById(event.mergeRequest.authorId!!)
            if (user.isSuccess) {
                repository.getUserByUsername(user.getOrThrow().username!!)?.let {
                    if (it.eventTypes!!.contains("merge")) {
                        bot.sendMessage(BotMessage(createMessage(event), it.chatId!!))
                    }
                }
            } else {
                logger.error { "User with id ${event.mergeRequest.authorId} not found" }
            }
        }
    }

    private fun createMessage(event: MergeRequestEvent): String {
        return "⚡️ Your merge request *bold*${event.mergeRequest!!.title}*bold* has been ${event.mergeRequest.status} by ${event.mergedByUser} ⚡️\n\n" +
                "Link: ${event.mergeRequest.url}" + "\n" +
                "Source branch: ${event.mergeRequest.sourceBranch}\n" +
                "Target branch: ${event.mergeRequest.targetBranch}\n"
    }
}