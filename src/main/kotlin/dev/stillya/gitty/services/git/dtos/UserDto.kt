package dev.stillya.gitty.services.git.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDto(@JsonProperty("username") val username: String?, @JsonProperty("name") val name: String?, @JsonProperty("id") val id: Int?)
