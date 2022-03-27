package dev.stillya.gitty.units

import dev.stillya.gitty.dtos.BotMessage
import dev.stillya.gitty.dtos.types.EventType
import dev.stillya.gitty.entities.TelegramUser
import dev.stillya.gitty.repositories.TelegramUserRepository
import dev.stillya.gitty.services.bot.telegram.GitlabTelegramBot
import dev.stillya.gitty.services.bot.telegram.TelegramMessageHandler
import dev.stillya.gitty.services.git.GitClient
import dev.stillya.gitty.dtos.UserDto
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TelegramMessageHandlerTest {
    private val bot: GitlabTelegramBot = mockk()
    private val telegramUserRepository: TelegramUserRepository = mockk()
    private val gitlabClient: GitClient = mockk()

    private val messageHandler = TelegramMessageHandler(bot, telegramUserRepository, gitlabClient)

    companion object {
        const val CHANNEL = "test"
        const val TOKEN = "test-token"
    }

    @Test
    fun `help and start handle message`() {
        runBlocking {
            every { bot.sendMessage(BotMessage(TelegramMessageHandler.HELP_MESSAGE, CHANNEL)) } returns Unit

            var result = messageHandler.handle(CHANNEL, "help")
            assertThat(result).isEqualTo(TelegramMessageHandler.HELP_MESSAGE)
            result = messageHandler.handle(CHANNEL, "/start")
            assertThat(result).isEqualTo(BotMessage(TelegramMessageHandler.HELP_MESSAGE, CHANNEL))
        }
    }

    @Test
    fun `merge handle message`() {
        // bot mock
        runBlocking {
            // gitlab client mock
            every { runBlocking { gitlabClient.getUser(TOKEN) } } returns kotlin.runCatching { UserDto("test", "test", 1) }
            // repository mock
            every {
                runBlocking {
                    telegramUserRepository.save(
                        TelegramUser(
                            CHANNEL, "test", "test", listOf(EventType.MERGE_REQUEST.value), 1, false
                        )
                    )
                }
            } returns null
            // bot mock
            every {
                bot.sendMessage(
                    BotMessage(
                        "You are now subscribed to ${EventType.MERGE_REQUEST.value} events", CHANNEL
                    )
                )
            } returns Unit

            val result = messageHandler.handle(CHANNEL, "subscribe merge $TOKEN")

            assertThat(result).isEqualTo(BotMessage("You are now subscribed to ${EventType.MERGE_REQUEST.value} events", CHANNEL))
        }
    }
}