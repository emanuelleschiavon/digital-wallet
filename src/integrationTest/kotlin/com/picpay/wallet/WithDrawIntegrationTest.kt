package com.picpay.wallet

import com.google.gson.Gson
import com.picpay.wallet.inbound.WalletRequest
import com.picpay.wallet.infra.AccountEntityBuilder
import com.picpay.wallet.infra.AccountRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class WithDrawIntegrationTest : TestContainerConfiguration() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @BeforeEach
    fun cleanUp() {
        accountRepository.deleteAll()
    }

    @Test
    fun `decreases balance when it withdraws money`() {
        val wallet = AccountEntityBuilder()
            .apply {
                this.balance = BigDecimal.valueOf(400)
                this.accountId = "001"
                this.id = 1
            }
            .build()
        accountRepository.save(wallet)
        val request = WalletRequest(BigDecimal.valueOf(400))

        mockMvc.perform(
            post("/wallets/${wallet.accountId}/withdraw")
                .contentType(APPLICATION_JSON)
                .content(Gson().toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("00"))

        val result = accountRepository.getReferenceById(wallet.id)
        assertEquals(BigDecimal.ZERO, result.balance)
    }
}