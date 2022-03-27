package dev.stillya.gitty.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class TelegramUser(
    @Id val chatId: String? = null,
    val username: String? = null,
    val name: String? = null,
    val eventTypes: List<String>? = null,
    val id: Int? = null,
    val isFinished: Boolean? = null
)
