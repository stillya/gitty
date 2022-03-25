package dev.stillya.gitty

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GittyApplication

fun main(args: Array<String>) {
	runApplication<GittyApplication>(*args)
}
