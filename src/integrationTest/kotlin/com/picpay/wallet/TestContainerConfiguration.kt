package com.picpay.wallet

import org.junit.ClassRule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

abstract class TestContainerConfiguration {

    companion object {
        @JvmField
        @ClassRule
        val dbContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:latest")
            .withDatabaseName("wallet")
            .withUsername("admin")
            .withPassword("password")

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", dbContainer::getJdbcUrl)
            registry.add("spring.datasource.username", dbContainer::getUsername)
            registry.add("spring.datasource.password", dbContainer::getPassword)
        }
        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            dbContainer.start()
        }
        @JvmStatic
        @AfterAll
        fun afterAll() {
            dbContainer.stop()
        }
    }
}