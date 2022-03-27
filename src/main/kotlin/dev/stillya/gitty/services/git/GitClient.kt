package dev.stillya.gitty.services.git

import dev.stillya.gitty.dtos.MergeRequestDto
import dev.stillya.gitty.dtos.MergeRequestHookDto
import dev.stillya.gitty.dtos.UserDto

interface GitClient {
    suspend fun getUser(token: String): Result<UserDto>
    suspend fun getMergeRequests(id: String, projectId: String): Result<MergeRequestDto>
}