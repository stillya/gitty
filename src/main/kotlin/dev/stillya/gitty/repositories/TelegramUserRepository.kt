package dev.stillya.gitty.repositories

import dev.stillya.gitty.entities.TelegramUser
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.core.*
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class TelegramUserRepository(private val mongo: ReactiveFluentMongoOperations) {

    fun getUserByChatId(chatId: String): Flow<TelegramUser> {
        return mongo.query<TelegramUser>().matching(query(where("chatId").isEqualTo(chatId)).addCriteria(where("isFinished").isEqualTo(false))).flow()
    }

    suspend fun save(user: TelegramUser): TelegramUser? = mongo.insert<TelegramUser>().oneAndAwait(user)
}