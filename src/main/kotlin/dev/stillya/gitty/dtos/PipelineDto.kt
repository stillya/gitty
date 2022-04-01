package dev.stillya.gitty.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PipelineDto(
    @JsonProperty("id") val id: String?,
    @JsonProperty("sha") val sha: String?,
    @JsonProperty("status") val status: String?,
    @JsonProperty("user") val user: UserDto?,
)