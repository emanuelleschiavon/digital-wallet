package com.picpay.wallet

import org.junit.ClassRule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service
import org.testcontainers.utility.DockerImageName


abstract class TestContainerConfiguration {

    companion object {
        @JvmField
        @ClassRule
        val dbContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:latest")
            .withDatabaseName("wallet")
            .withUsername("admin")
            .withPassword("password")

        private val dockerImageName = DockerImageName.parse("localstack/localstack:latest")

        @JvmField
        @ClassRule
        val topicContainer: LocalStackContainer = LocalStackContainer(dockerImageName)
            .withServices(Service.SNS)
            .withEnv("AWS_ACCESS_KEY_ID", "access-key")
            .withEnv("AWS_SECRET_ACCESS_KEY", "secret-key")

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", dbContainer::getJdbcUrl)
            registry.add("spring.datasource.username", dbContainer::getUsername)
            registry.add("spring.datasource.password", dbContainer::getPassword)
            registry.add("cloud.aws.endpoint.uri") { topicContainer.endpoint }
            registry.add("cloud.aws.region") { topicContainer.region }

        }

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            topicContainer.start()
            dbContainer.start()
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            dbContainer.stop()
            topicContainer.stop()
        }
    }
}