package dev.stillya.gitty.services.git

import com.fasterxml.jackson.databind.ObjectMapper
import dev.stillya.gitty.configurations.GitlabProperties
import dev.stillya.gitty.dtos.MergeRequestDto
import dev.stillya.gitty.dtos.MergeRequestHookDto
import dev.stillya.gitty.dtos.UserDto
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import javax.annotation.PostConstruct

@Component
class GitlabClient(
    private val mapper: ObjectMapper,
    private val props: GitlabProperties
) : GitClient {

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

    override suspend fun getMergeRequests(id: String, projectId: String): Result<MergeRequestDto> {
        return kotlin.runCatching {
            val result = httpClient.get()
                .uri { it.path("/api/v4/projects/$projectId/merge_requests/$id").build() }
                .header("PRIVATE-TOKEN", props.token)
                .retrieve()
                .awaitBody<MergeRequestDto>()
            return Result.success(result)
        }
    }
}