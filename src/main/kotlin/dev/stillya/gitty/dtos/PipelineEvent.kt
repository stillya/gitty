package dev.stillya.gitty.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PipelineEvent(
    @JsonProperty("status") val status: String?,
    @JsonProperty("url") val url: String?,
    @JsonProperty("mr_id") val mergeRequestId: String?,
    @JsonProperty("project_id") val projectId: String?
)