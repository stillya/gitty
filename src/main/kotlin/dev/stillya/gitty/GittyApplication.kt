package dev.stillya.gitty

import dev.stillya.gitty.configurations.GitlabProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(value = [GitlabProperties::class])
@SpringBootApplication
class GittyApplication

fun main(args: Array<String>) {
    runApplication<GittyApplication>(*args)
}
