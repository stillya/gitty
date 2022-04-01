package dev.stillya.gitty.services.git

import dev.stillya.gitty.dtos.MergeRequestDto
import dev.stillya.gitty.dtos.UserDto

interface GitClient {
    suspend fun getUser(token: String): Result<UserDto>
    suspend fun getMergeRequest(pipelineId: String, projectId: String): Result<MergeRequestDto>
    suspend fun getUserById(userId: String): Result<UserDto>
}