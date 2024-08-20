package com.picpay.wallet.inbound

import com.picpay.wallet.builders.TransactionBuilder
import com.picpay.wallet.builders.TransactionRequestBuilder
import com.picpay.wallet.builders.TransactionTransferRequestBuilder
import com.picpay.wallet.domain.Transaction.TransactionType.DEPOSIT
import com.picpay.wallet.domain.Transaction.TransactionType.WITHDRAW
import com.picpay.wallet.domain.services.TransactionService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class TransactionControllerTest {

    @InjectMockKs
    private lateinit var transactionController: TransactionController

    @MockK
    private lateinit var transactionService: TransactionService

    private val request = TransactionRequestBuilder().build()

    @Test
    fun `withdraws money`() {
        val transaction = TransactionBuilder().apply { this.type = WITHDRAW }.build()
        every { transactionService.withdraw(request) } returns transaction

        val result = transactionController.withdraw(request)

        assertEquals(transaction, result)
    }

    @Test
    fun `deposits money`() {
        val transaction = TransactionBuilder().apply { this.type = DEPOSIT }.build()
        every { transactionService.deposit(request) } returns  transaction

        val result = transactionController.deposit(request)

        assertEquals(transaction, result)
    }

    @Test
    fun `pay money`() {
        val transaction = TransactionBuilder().apply { this.type = DEPOSIT }.build()
        every { transactionService.payment(request) }  returns  transaction

        val result = transactionController.payment(request)

        assertEquals(transaction, result)
    }

    @Test
    fun `transfer money`() {
        val transaction = TransactionBuilder().apply { this.type = DEPOSIT }.build()
        val transferRequest = TransactionTransferRequestBuilder().build()
        every { transactionService.transfer(transferRequest) }  returns  transaction

        val result = transactionController.transfer(transferRequest)

        assertEquals(transaction, result)
    }
}