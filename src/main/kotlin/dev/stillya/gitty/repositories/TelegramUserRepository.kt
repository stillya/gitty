package dev.stillya.gitty.repositories

import dev.stillya.gitty.entities.TelegramUser
import org.springframework.data.mongodb.core.*
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class TelegramUserRepository(private val mongo: ReactiveFluentMongoOperations) {

    suspend fun getUserByChatId(chatId: String): TelegramUser? {
        return mongo.query<TelegramUser>().matching(query(where("chatId").isEqualTo(chatId)))
            .awaitOneOrNull()
    }

    suspend fun getUserByUsername(username: String): TelegramUser? {
        return mongo.query<TelegramUser>().matching(query(where("username").isEqualTo(username)))
            .awaitOneOrNull()
    }

    suspend fun update(user: TelegramUser) {
        mongo.update<TelegramUser>().replaceWith(user).asType<TelegramUser>().findReplaceAndAwait()
    }

    suspend fun save(user: TelegramUser): TelegramUser? = mongo.insert<TelegramUser>().oneAndAwait(user)

    suspend fun deleteByChatId(chatId: String) {
        mongo.remove<TelegramUser>().matching(query(where("chatId").isEqualTo(chatId)))
    }
}