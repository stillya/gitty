package dev.stillya.gitty.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class RepositoryDto(@JsonProperty("name") val name: String?, @JsonProperty("url") val url: String?)
