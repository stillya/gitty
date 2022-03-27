package dev.stillya.gitty.units

import dev.stillya.gitty.dtos.BotMessage
import dev.stillya.gitty.dtos.types.EventType
import dev.stillya.gitty.entities.TelegramUser
import dev.stillya.gitty.repositories.TelegramUserRepository
import dev.stillya.gitty.services.bot.telegram.GitlabTelegramBot
import dev.stillya.gitty.services.bot.telegram.TelegramMessageHandler
import dev.stillya.gitty.services.git.GitClient
import dev.stillya.gitty.services.git.dtos.UserDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
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

            messageHandler.handle(CHANNEL, "help")
            messageHandler.handle(CHANNEL, "/start")

            verify(exactly = 2) { bot.sendMessage(BotMessage(TelegramMessageHandler.HELP_MESSAGE, CHANNEL)) }
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

            messageHandler.handle(CHANNEL, "subscribe merge $TOKEN")

            verify(exactly = 1) { bot.sendMessage(BotMessage("You are now subscribed to ${EventType.MERGE_REQUEST.value} events", CHANNEL)) }
        }
    }
}