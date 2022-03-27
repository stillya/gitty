package dev.stillya.gitty.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserDto(@JsonProperty("username") val username: String?, @JsonProperty("name") val name: String?, @JsonProperty("id") val id: Int?)
