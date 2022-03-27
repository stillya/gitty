package dev.stillya.gitty.services.bot.telegram

import dev.stillya.gitty.dtos.BotMessage
import dev.stillya.gitty.dtos.types.EventType
import dev.stillya.gitty.entities.TelegramUser
import dev.stillya.gitty.repositories.TelegramUserRepository
import dev.stillya.gitty.services.git.GitClient
import org.springframework.stereotype.Service

@Service
class TelegramMessageHandler(
    private val bot: GitlabTelegramBot,
    private val telegramUserRepository: TelegramUserRepository,
    private val gitlabClient: GitClient
) {
    suspend fun handle(channel: String, message: String) {
        val messageParts = message.split(" ")
        val command = messageParts[0]
        val args = messageParts.drop(1)

        when (command) {
            "/start" -> bot.sendMessage(
                BotMessage(
                    channel, HELP_MESSAGE
                )
            )
            "help" -> bot.sendMessage(
                BotMessage(
                    channel, HELP_MESSAGE
                )
            )
            "subscribe" -> {
                val type = args[0]
                val token = args[1]
                if (EventType.valueOf(type) == EventType.UNKNOWN) {
                    bot.sendMessage(
                        BotMessage(
                            channel, "Unknown event type: $type"
                        )
                    )
                    return
                }
                if (type.isNotEmpty() && token.isNotEmpty()) {
                    gitlabClient.getUser(token).onFailure {
                        bot.sendMessage(
                            BotMessage(
                                channel, "Error happened while getting user: ${it.message}"
                            )
                        )
                    }.onSuccess {
                        telegramUserRepository.save(TelegramUser(channel, it.username, it.name, listOf(type)))
                        bot.sendMessage(
                            BotMessage(
                                channel, "You are now subscribed to $type events"
                            )
                        )
                    }
                } else {
                    bot.sendMessage(
                        BotMessage(
                            channel, "You not provide type or token"
                        )
                    )
                }


            }
        }


    }

    companion object {
        const val HELP_MESSAGE = "Hello, I'm Gitty!\n" +
                "I'm a bot that can help you manage your Gitlab repositories.\n" +
                "You can use me to get notification about pipelines and merge requests\n" +
                "---------------------------HOW TO USE IT?----------------------------\n" +
                "1.SUBSCRIBE(any register): subscribe <event> <GITLAB-TOKEN>\n" +
                "2. UNSUBSCRIBE(Any register): unsubscribe\n" +
                "3. HELP(Any register): help\n" +
                "---------------------------TYPE OF EVENTS----------------------------\n" +
                "1.For getting notification about pipelines: pipeline\n" +
                "2.For getting notification about merge requests: merge\n" +
                "---------------------------EXAMPLES----------------------------\n" +
                "> subscribe pipeline Kv3dTo1epDsvcqH9MiSK\n" +
                "> subscribe merge\n" +
                "> unsubscribe\n" +
                "> help\n"

        fun from(type: String?): EventType = EventType.values().find { it.name == type } ?: EventType.UNKNOWN
    }
}
