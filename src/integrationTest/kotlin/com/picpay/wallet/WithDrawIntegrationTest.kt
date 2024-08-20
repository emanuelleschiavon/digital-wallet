package com.picpay.wallet

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

    @BeforeEach
    fun cleanUp() {
        accountRepository.deleteAll()
    }

    @Test
    fun `decreases balance when it withdraws money`() {
        val account = AccountBuilder().build()
        accountRepository.save(account)

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
            .andExpect(jsonPath("$.sourceAccountId").value(account.accountId))

        val accountResult = accountRepository.getReferenceById(account.id)
        val transactionResult = transactionRepository.findAll().first()
        assertEquals(BigDecimal.ZERO, accountResult.balance)
        assertEquals(account.balance, transactionResult.amount)
    }
}