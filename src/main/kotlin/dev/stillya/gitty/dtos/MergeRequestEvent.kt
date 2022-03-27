package dev.stillya.gitty.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MergeRequestEvent(
    @JsonProperty("user") val user: UserDto?,
    @JsonProperty("repository") val repository: RepositoryDto?,
    @JsonProperty("object_attributes") val mergeRequest: MergeRequestDto?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MergeRequestDto(
    @JsonProperty("iid") val id: Int?,
    @JsonProperty("source_branch") val sourceBranch: String?,
    @JsonProperty("target_branch") val targetBranch: String?,
    @JsonProperty("url") val url: String?,
    @JsonProperty("state") val status: String?,
    @JsonProperty("title") val title: String?
)