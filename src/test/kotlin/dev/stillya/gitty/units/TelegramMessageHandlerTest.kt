package dev.stillya.gitty.units

import dev.stillya.gitty.dtos.BotMessage
import dev.stillya.gitty.dtos.UserDto
import dev.stillya.gitty.dtos.types.EventType
import dev.stillya.gitty.entities.TelegramUser
import dev.stillya.gitty.repositories.TelegramUserRepository
import dev.stillya.gitty.services.bot.telegram.TelegramMessageHandler
import dev.stillya.gitty.services.git.GitClient
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TelegramMessageHandlerTest {
    private val telegramUserRepository: TelegramUserRepository = mockk()
    private val gitlabClient: GitClient = mockk()

    private val messageHandler = TelegramMessageHandler(telegramUserRepository, gitlabClient)

    companion object {
        const val CHANNEL = "test"
        const val TOKEN = "test-token"
    }

    @Test
    fun `help and start handle message`() {
        runBlocking {
            var result = messageHandler.handle(CHANNEL, "help")
            assertThat(result).isEqualTo(BotMessage(TelegramMessageHandler.HELP_MESSAGE, CHANNEL))
            result = messageHandler.handle(CHANNEL, "/start")
            assertThat(result).isEqualTo(BotMessage(TelegramMessageHandler.HELP_MESSAGE, CHANNEL))
        }
    }

    @Test
    fun `merge handle message`() {
        runBlocking {
            // gitlab client mock
            every { runBlocking { gitlabClient.getUser(TOKEN) } } returns kotlin.runCatching { UserDto("test", "test", 1) }
            // repository mock
            every {
                runBlocking {
                    telegramUserRepository.save(
                        TelegramUser(
                            CHANNEL, "test", "test", listOf(EventType.MERGE_REQUEST.value), 1
                        )
                    )
                }
            } returns null
            every {
                runBlocking {
                    telegramUserRepository.getUserByChatId(CHANNEL)
                }
            } returns null

            val result = messageHandler.handle(CHANNEL, "subscribe merge $TOKEN")

            assertThat(result).isEqualTo(BotMessage("You are now subscribed to ${EventType.MERGE_REQUEST.value} events", CHANNEL))
        }
    }

    @Test
    fun `duplicate merge handle message`() {
        runBlocking {
            // gitlab client mock
            every { runBlocking { gitlabClient.getUser(TOKEN) } } returns kotlin.runCatching { UserDto("test", "test", 1) }
            // repository mock
            every {
                runBlocking {
                    telegramUserRepository.save(
                        TelegramUser(
                            CHANNEL, "test", "test", listOf(EventType.MERGE_REQUEST.value), 1
                        )
                    )
                }
            } returns null
            every {
                runBlocking {
                    telegramUserRepository.getUserByChatId(CHANNEL)
                }
            } returns TelegramUser(
                CHANNEL, "test", "test", listOf(EventType.MERGE_REQUEST.value), 1
            )

            val result = messageHandler.handle(CHANNEL, "subscribe merge $TOKEN")

            assertThat(result).isEqualTo(BotMessage("You already subscribed to ${EventType.MERGE_REQUEST.value} events", CHANNEL))
        }
    }
}