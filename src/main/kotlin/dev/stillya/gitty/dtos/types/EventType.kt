package dev.stillya.gitty.dtos.types

enum class EventType(val value: String) {
    PIPELINE("pipeline"),
    MERGE_REQUEST("merge"),
    UNKNOWN("unknown");
}