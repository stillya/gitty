package dev.stillya.gitty.services.bot.dto

data class MergeRequestEvent(val username: String, val title: String, val url: String, val state: String)