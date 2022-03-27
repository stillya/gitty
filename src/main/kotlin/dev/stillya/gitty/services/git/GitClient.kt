package dev.stillya.gitty.services.git

import dev.stillya.gitty.dtos.UserDto

interface GitClient {
    suspend fun getUser(token: String): Result<UserDto>
}