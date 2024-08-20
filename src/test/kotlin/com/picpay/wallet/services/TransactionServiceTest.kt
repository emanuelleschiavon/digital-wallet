package com.picpay.wallet.services

import com.picpay.wallet.builders.AccountBuilder
import com.picpay.wallet.builders.TransactionBuilder
import com.picpay.wallet.builders.TransactionRequestBuilder
import com.picpay.wallet.builders.TransactionTransferRequestBuilder
import com.picpay.wallet.domain.services.TransactionService
import com.picpay.wallet.infra.AccountRepository
import com.picpay.wallet.domain.Transaction.TransactionType
import com.picpay.wallet.infra.TransactionPublisher
import com.picpay.wallet.infra.TransactionRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class TransactionServiceTest {

    @InjectMockKs
    private lateinit var transactionService: TransactionService

    @MockK
    private lateinit var accountRepository: AccountRepository

    @MockK
    private lateinit var transactionRepository: TransactionRepository

    @MockK
    private lateinit var transactionPublisher: TransactionPublisher

    private val account = AccountBuilder().apply { this.balance = BigDecimal.valueOf(200) }.build()
    private val request = TransactionRequestBuilder().build()

    @Test
    fun `decreases balance from account when operation is withdraw`() {
        val accountUpdated = AccountBuilder()
            .apply { this.balance = BigDecimal.valueOf(100) }
            .build()
        val transaction = TransactionBuilder()
            .apply {
                this.type = TransactionType.WITHDRAW
                this.date = request.date
                this.sourceAccount = accountUpdated
            }
            .build()
        every { accountRepository.getReferenceById(account.accountId) } returns account
        every { accountRepository.save(accountUpdated) } returns accountUpdated
        every { transactionRepository.save(transaction) } returns transaction
        every { transactionPublisher.publish(transaction) } returns transaction

        val result = transactionService.withdraw(request)

        assertEquals(result, transaction)
    }

    @Test
    fun `decreases balance from account when operation is payment`() {
        val accountUpdated = AccountBuilder().apply { this.balance = BigDecimal.valueOf(100) }.build()
        val transaction = TransactionBuilder()
            .apply {
                this.type = TransactionType.PAYMENT
                this.date = request.date
                this.sourceAccount = accountUpdated
            }
            .build()
        every { accountRepository.getReferenceById(account.accountId) } returns account
        every { accountRepository.save(accountUpdated) } returns accountUpdated
        every { transactionRepository.save(transaction) } returns transaction
        every { transactionPublisher.publish(transaction) } returns transaction

        transactionService.payment(request)

        verify(exactly = 1) { accountRepository.save(accountUpdated) }
        verify(exactly = 1) { transactionRepository.save(transaction) }

    }

    @Test
    fun `increases balance from account when operation is deposit`() {
        val accountUpdated = AccountBuilder().apply { this.balance = BigDecimal.valueOf(300) }.build()
        val transaction = TransactionBuilder()
            .apply {
                this.type = TransactionType.DEPOSIT
                this.date = request.date
                this.sourceAccount = accountUpdated
            }
            .build()
        every { accountRepository.getReferenceById(account.accountId) } returns account
        every { accountRepository.save(accountUpdated) } returns accountUpdated
        every { transactionRepository.save(transaction) } returns transaction
        every { transactionPublisher.publish(transaction) } returns transaction

        transactionService.deposit(request)

        verify(exactly = 1) { accountRepository.save(accountUpdated) }
        verify(exactly = 1) { transactionRepository.save(transaction) }
    }

    @Test
    fun `increases balance from target account and decreases balance from source account when operation is deposit`() {
        val targetAccount = AccountBuilder()
            .apply {
                this.balance = BigDecimal.valueOf(200)
                this.accountId = "456"
            }
            .build()
        val sourceAccountUpdated = AccountBuilder()
            .apply { this.balance = BigDecimal.valueOf(100) }
            .build()
        val targetAccountUpdated = AccountBuilder()
            .apply {
                this.balance = BigDecimal.valueOf(300)
                this.accountId = "456"
            }
            .build()
        val transferRequest = TransactionTransferRequestBuilder().build()
        val transaction = TransactionBuilder()
            .apply {
                this.type = TransactionType.TRANSFER
                this.targetAccountId = targetAccount.accountId
                this.date = transferRequest.date
                this.sourceAccount = account
            }
            .build()
        every { accountRepository.getReferenceById(account.accountId) } returns account
        every { accountRepository.getReferenceById(targetAccount.accountId) } returns targetAccount
        every { accountRepository.save(sourceAccountUpdated) } returns sourceAccountUpdated
        every { accountRepository.save(targetAccountUpdated) } returns targetAccountUpdated
        every { transactionRepository.save(transaction) } returns transaction
        every { transactionPublisher.publish(transaction) } returns transaction

        transactionService.transfer(transferRequest)

        verify(exactly = 1) { accountRepository.save(sourceAccountUpdated) }
        verify(exactly = 1) { accountRepository.save(targetAccountUpdated) }
        verify(exactly = 1) { transactionRepository.save(transaction) }
    }
}