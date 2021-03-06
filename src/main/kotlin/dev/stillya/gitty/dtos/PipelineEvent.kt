package dev.stillya.gitty.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PipelineEvent(
    @JsonProperty("status") val status: String?,
    @JsonProperty("url") val url: String?,
    @JsonProperty("pipeline_id") val pipelineId: String?,
    @JsonProperty("project_id") val projectId: String?,
)