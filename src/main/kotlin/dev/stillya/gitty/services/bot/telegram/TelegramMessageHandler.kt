package dev.stillya.gitty.services.bot.telegram

import dev.stillya.gitty.dtos.BotMessage
import dev.stillya.gitty.dtos.types.EventType
import dev.stillya.gitty.entities.TelegramUser
import dev.stillya.gitty.repositories.TelegramUserRepository
import dev.stillya.gitty.services.git.GitClient
import org.springframework.stereotype.Service

@Service
class TelegramMessageHandler(
    private val telegramUserRepository: TelegramUserRepository,
    private val gitlabClient: GitClient
) {
    suspend fun handle(channel: String, message: String): BotMessage {
        val messageParts = message.split(" ")
        val command = messageParts[0]
        val args = messageParts.drop(1)

        return when (command) {
            "/start" ->
                BotMessage(
                    HELP_MESSAGE, channel
                )
            "help" ->
                BotMessage(
                    HELP_MESSAGE, channel
                )
            "subscribe" -> {
                if (args.size != 2) {
                    return BotMessage(
                        "Invalid arguments. Usage: subscribe <event> <GITLAB-TOKEN>", channel
                    )
                }
                val type = args[0]
                val token = args[1]
                if (from(type) == EventType.UNKNOWN) {
                    return BotMessage(
                        "Unknown event type: $type", channel
                    )
                }
                if (type.isNotEmpty() && token.isNotEmpty()) {
                    val user = gitlabClient.getUser(token)

                    return if (user.isFailure) {
                        BotMessage(
                            "Error happened while getting user: ${user.exceptionOrNull()}", channel
                        )
                    } else {
                        telegramUserRepository.getUserByChatId(channel)?.let {
                            if (it.eventTypes!!.contains(type)) {
                                return BotMessage(
                                    "You already subscribed to $type events", channel
                                )
                            } else {
                                telegramUserRepository.update(
                                    TelegramUser(it.chatId, it.username, it.name, it.eventTypes.plus(type), it.id, it.isFinished)
                                )
                                BotMessage(
                                    "You are now subscribed to $type events too", channel
                                )
                            }
                        }?: run {
                            telegramUserRepository.save(
                                TelegramUser(
                                    channel,
                                    user.getOrThrow().username,
                                    user.getOrThrow().name,
                                    listOf(type),
                                    user.getOrThrow().id,
                                    false
                                )
                            )
                            return BotMessage(
                                "You are now subscribed to $type events", channel
                            )
                        }
                    }
                } else {
                    return BotMessage(
                        "You not provide type or token", channel
                    )

                }
            }
            else -> BotMessage(
                "Unknown command: $command", channel
            )
        }
    }


    companion object {
        const val HELP_MESSAGE = "Hello, I'm Gitty!\n" +
                "I'm a bot that can help you manage your Gitlab repositories.\n" +
                "You can use me to get notification about pipelines and merge requests\n" +
                "---------------------------HOW TO USE IT?----------------------------\n" +
                "1.SUBSCRIBE(any register): subscribe <event> <GITLAB-TOKEN>\n" +
                "2.UNSUBSCRIBE(Any register): unsubscribe\n" +
                "3.HELP(Any register): help\n" +
                "---------------------------TYPE OF EVENTS----------------------------\n" +
                "1.For getting notification about pipelines: pipeline\n" +
                "2.For getting notification about merge requests: merge\n" +
                "---------------------------EXAMPLES----------------------------\n" +
                "> subscribe pipeline Kv3dTo1epDsvcqH9MiSK\n" +
                "> subscribe merge Kv3dTo1epDsvcqH9MiSK\n" +
                "> unsubscribe\n" +
                "> help\n"

        fun from(type: String?): EventType = EventType.values().find { it.value == type } ?: EventType.UNKNOWN
    }
}