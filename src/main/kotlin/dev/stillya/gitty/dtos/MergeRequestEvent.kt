package dev.stillya.gitty.dtos

data class MergeRequestEvent(val username: String, val title: String, val url: String, val state: String)