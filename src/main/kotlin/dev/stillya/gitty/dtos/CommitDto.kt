package dev.stillya.gitty.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class CommitDto(
    @JsonProperty("message") val message: String?
)