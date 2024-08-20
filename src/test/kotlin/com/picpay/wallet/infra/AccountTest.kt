package com.picpay.wallet.infra

import com.picpay.wallet.builders.AccountBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals

class AccountTest {

    @Test
    fun `decreases balance`() {
        val wallet = AccountBuilder()
            .apply { this.balance = BigDecimal.valueOf(100) }
            .build()

        val result = wallet.decreaseBalance(BigDecimal.TEN)

        assertEquals(BigDecimal.valueOf(90), result.balance)
    }

    @Test
    fun `increases balance`() {
        val wallet = AccountBuilder()
            .apply { this.balance = BigDecimal.valueOf(100) }
            .build()

        val result = wallet.increaseBalance(BigDecimal.TEN)

        assertEquals(BigDecimal.valueOf(110), result.balance)
    }

    @Test
    fun `throws exception when balance is less than value`() {
        val wallet = AccountBuilder().apply { this.balance = BigDecimal.valueOf(10) }.build()

        assertThrows<Exception> { wallet.decreaseBalance(BigDecimal.valueOf(11)) }
    }
}