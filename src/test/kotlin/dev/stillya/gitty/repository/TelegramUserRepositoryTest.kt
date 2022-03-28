package dev.stillya.gitty.repository

import dev.stillya.gitty.entities.TelegramUser
import dev.stillya.gitty.repositories.TelegramUserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations
import org.springframework.data.mongodb.core.allAndAwait
import org.springframework.data.mongodb.core.insert
import org.springframework.data.mongodb.core.remove


@DataMongoTest
class TelegramUserRepositoryTest : AbstractMongoTest() {

    @Autowired
    private lateinit var repository: TelegramUserRepository

    @Autowired
    private lateinit var mongo: ReactiveFluentMongoOperations

    @AfterEach
    fun cleanUp() {
        runBlocking {
            mongo.remove<TelegramUser>().allAndAwait()
        }
    }

    @Test
    fun `get user by chat id`() {

        mongo.insert<TelegramUser>().one(TelegramUser("1", "firstName", "secondName", listOf("merge"), 1)).block()

        runBlocking {
            val user = repository.getUserByChatId("1")!!
            assertEquals("1", user.chatId)
            assertEquals("firstName", user.username)
            assertEquals("secondName", user.name)
            assertEquals("merge", user.eventTypes!![0])
            assertEquals(1, user.id)
        }

    }

    @Test
    fun save() {
        runBlocking {
            repository.save(TelegramUser("1", "firstName", "secondName", listOf("merge"), 1))

            val user = repository.getUserByChatId("1")!!
            assertEquals("1", user.chatId)
            assertEquals("firstName", user.username)
            assertEquals("secondName", user.name)
            assertEquals("merge", user.eventTypes!![0])
            assertEquals(1, user.id)
        }
    }


    @TestConfiguration
    class TestConfig {
        @Autowired
        private lateinit var mongo: ReactiveFluentMongoOperations

        @Bean
        fun repository() = TelegramUserRepository(mongo)
    }
}