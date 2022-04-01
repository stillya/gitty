package dev.stillya.gitty.services.git

import dev.stillya.gitty.configurations.GitlabProperties
import dev.stillya.gitty.dtos.CommitDto
import dev.stillya.gitty.dtos.MergeRequestDto
import dev.stillya.gitty.dtos.PipelineDto
import dev.stillya.gitty.dtos.UserDto
import mu.KLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import javax.annotation.PostConstruct

@Component
class GitlabClient(
    private val props: GitlabProperties
) : GitClient {

    companion object : KLogging()

    private lateinit var httpClient: WebClient

    @PostConstruct
    fun init() {
        httpClient = WebClient.builder().baseUrl(props.baseUrl).build()
    }

    override suspend fun getUser(token: String): Result<UserDto> {
        return kotlin.runCatching {
            val result = httpClient.get()
                .uri { it.path("/api/v4/user").build() }
                .header("Private-Token", token)
                .retrieve()
                .awaitBody<UserDto>()
            return Result.success(result)
        }
    }

    override suspend fun getUserById(userId: String): Result<UserDto> {
        return kotlin.runCatching {
            val result = httpClient.get()
                .uri { it.path("/api/v4/users/$userId").build() }
                .header("Private-Token", props.token)
                .retrieve()
                .awaitBody<UserDto>()
            return Result.success(result)
        }
    }

    // This is fcking crutch because in my case pipeline is running on already merged MR and Gitlab API has a bug with pipeline related MR
    override suspend fun getMergeRequest(pipelineId: String, projectId: String): Result<MergeRequestDto> {
        return kotlin.runCatching {
            try {
                val pipeline = httpClient.get().uri { it.path("/api/v4/projects/$projectId/pipelines/$pipelineId").build() }
                    .header("Private-Token", props.token)
                    .retrieve()
                    .awaitBody<PipelineDto>()

                val commit = httpClient.get().uri { it.path("/api/v4/projects/$projectId/repository/commits/${pipeline.sha}").build() }
                    .header("Private-Token", props.token)
                    .retrieve()
                    .awaitBody<CommitDto>()
                val numberOfMergeRequest = commit.message?.split("!")?.last()?.toIntOrNull()

                return if (numberOfMergeRequest != null) {
                    val mergeRequest = httpClient.get().uri { it.path("/api/v4/projects/$projectId/merge_requests/$numberOfMergeRequest").build() }
                        .header("Private-Token", props.token)
                        .retrieve()
                        .awaitBody<MergeRequestDto>()
                    Result.success(mergeRequest)
                } else {
                    Result.success(MergeRequestDto(null, null, null, null, pipeline.user))
                }
            } catch (e: Exception) {
                logger.error { e }
                return Result.failure(e)
            }
        }
    }
}