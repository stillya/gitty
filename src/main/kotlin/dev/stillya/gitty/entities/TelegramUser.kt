package dev.stillya.gitty.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class TelegramUser(
    @Id val chatId: String? = null,
    val firstName: String? = null,
    val secondName: String? = null,
    val isFinished: Boolean? = null
)
