package dev.stillya.gitty.services.git

import dev.stillya.gitty.services.git.dtos.UserDto

interface GitClient {
    suspend fun getUser(token: String): Result<UserDto>
}