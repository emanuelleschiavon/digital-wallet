package com.picpay.wallet

import com.amazonaws.services.sns.AmazonSNS
import com.picpay.wallet.builders.AccountBuilder
import com.picpay.wallet.infra.AccountRepository
import com.picpay.wallet.infra.TransactionRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class WithDrawIntegrationTest : TestContainerConfiguration() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    @Autowired
    private lateinit var sns: AmazonSNS

    private val account = AccountBuilder().apply { this.accountId = "123" }.build()

    @BeforeEach
    fun cleanUp() {
        sns.createTopic("transaction-done")
        transactionRepository.deleteAll()
        accountRepository.deleteAll()
        accountRepository.save(account)
    }

    @Test
    fun `decreases balance when it withdraws money`() {
        mockMvc.perform(
            post("/transactions/withdraw")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                        "sourceAccountId": "${account.accountId}",
                        "date": "2020-03-01T22:00:00",
                        "value": ${account.balance}
                    }
                """.trimIndent())
        )
            .andExpect(status().isOk())

        val accountResult = accountRepository.getReferenceById(account.accountId)
        val transactionResult = transactionRepository.findAll().first()
        assertEquals(BigDecimal.ZERO, accountResult.balance)
        assertEquals(account.balance, transactionResult.amount)
    }

    @Test
    fun `increases balance when it deposits money`() {
        mockMvc.perform(
            post("/transactions/deposit")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                        "sourceAccountId": "${account.accountId}",
                        "date": "2020-03-01T22:00:00",
                        "value": ${account.balance}
                    }
                """.trimIndent())
        )
            .andExpect(status().isOk())

        val accountResult = accountRepository.getReferenceById(account.accountId)
        val transactionResult = transactionRepository.findAll().first()
        assertEquals(BigDecimal.valueOf(40000), accountResult.balance)
        assertEquals(account.balance, transactionResult.amount)
    }

    @Test
    fun `decreases balance when it pays money`() {
        mockMvc.perform(
            post("/transactions/payment")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                        "sourceAccountId": "${account.accountId}",
                        "date": "2020-03-01T22:00:00",
                        "value": ${account.balance}
                    }
                """.trimIndent())
        )
            .andExpect(status().isOk())

        val accountResult = accountRepository.getReferenceById(account.accountId)
        val transactionResult = transactionRepository.findAll().first()
        assertEquals(BigDecimal.ZERO, accountResult.balance)
        assertEquals(account.balance, transactionResult.amount)
    }

    @Test
    fun `decreases source balance and increase target balance when it transfers money`() {
        val targetAccount = AccountBuilder().apply { this.accountId = "456" }.build()
        accountRepository.save(targetAccount)
        mockMvc.perform(
            post("/transactions/transfer")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                        "sourceAccountId": "${account.accountId}",
                        "targetAccountId": "${targetAccount.accountId}",
                        "date": "2020-03-01T22:00:00",
                        "value": ${account.balance}
                    }
                """.trimIndent())
        )
            .andExpect(status().isOk())

        val accountResult = accountRepository.getReferenceById(account.accountId)
        val targetAccountResult = accountRepository.getReferenceById(targetAccount.accountId)
        val transactionResult = transactionRepository.findAll().first()
        assertEquals(BigDecimal.ZERO, accountResult.balance)
        assertEquals(BigDecimal.valueOf(40000), targetAccountResult.balance)
        assertEquals(account.balance, transactionResult.amount)
    }
}