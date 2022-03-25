package dev.stillya.gitty.repository

import org.junit.runner.RunWith
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
@DataMongoTest(excludeAutoConfiguration = [EmbeddedMongoAutoConfiguration::class])
@RunWith(SpringRunner::class)
open class AbstractMongoTest {
    companion object {
        @Container
        var mongoDBContainer = MongoDBContainer("mongo:4.4.2")

        @DynamicPropertySource
        @JvmStatic
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri") { mongoDBContainer.replicaSetUrl }
        }
    }
}