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
                    HELP_MESSAGE, channel
                )
            )
            "help" -> bot.sendMessage(
                BotMessage(
                    HELP_MESSAGE, channel
                )
            )
            "subscribe" -> {
                val type = args[0]
                val token = args[1]
                if (from(type) == EventType.UNKNOWN) {
                    bot.sendMessage(
                        BotMessage(
                            "Unknown event type: $type", channel
                        )
                    )
                    return
                }
                if (type.isNotEmpty() && token.isNotEmpty()) {
                    gitlabClient.getUser(token).onFailure {
                        bot.sendMessage(
                            BotMessage(
                                "Error happened while getting user: ${it.message}", channel
                            )
                        )
                    }.onSuccess {
                        telegramUserRepository.save(TelegramUser(channel, it.username, it.name, listOf(type), it.id, false))
                        bot.sendMessage(
                            BotMessage(
                                "You are now subscribed to $type events", channel
                            )
                        )
                    }
                } else {
                    bot.sendMessage(
                        BotMessage(
                            "You not provide type or token", channel
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
                "> subscribe merge Kv3dTo1epDsvcqH9MiSK\n" +
                "> unsubscribe\n" +
                "> help\n"

        fun from(type: String?): EventType = EventType.values().find { it.value == type } ?: EventType.UNKNOWN
    }
}
