package com.picpay.wallet.services

import com.picpay.wallet.builders.AccountBuilder
import com.picpay.wallet.builders.TransactionBuilder
import com.picpay.wallet.builders.TransactionRequestBuilder
import com.picpay.wallet.builders.TransactionTransferRequestBuilder
import com.picpay.wallet.infra.AccountRepository
import com.picpay.wallet.infra.Transaction.TransactionType
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

    private val account = AccountBuilder().apply { this.balance = BigDecimal.valueOf(200) }.build()
    private val request = TransactionRequestBuilder().build()

    @Test
    fun `decreases balance from wallet when operation is withdraw`() {
        val accountUpdated = AccountBuilder()
            .apply { this.balance = BigDecimal.valueOf(100) }
            .build()
        val transaction = TransactionBuilder()
            .apply {
                this.type = TransactionType.WITHDRAW
                this.date = request.date
            }
            .build()
        every { accountRepository.getByAccountId(account.accountId) } returns account
        every { accountRepository.save(accountUpdated) } returns accountUpdated
        every { transactionRepository.save(transaction) } returns transaction

        val result = transactionService.withdraw(request)

        assertEquals(result, transaction)
    }

    @Test
    fun `decreases balance from wallet when operation is payment`() {
        val walletUpdated = AccountBuilder().apply { this.balance = BigDecimal.valueOf(100) }.build()
        val transaction = TransactionBuilder()
            .apply {
                this.type = TransactionType.PAYMENT
                this.date = request.date
            }
            .build()
        every { accountRepository.getByAccountId(account.accountId) } returns account
        every { accountRepository.save(walletUpdated) } returns walletUpdated
        every { transactionRepository.save(transaction) } returns transaction

        transactionService.payment(request)

        verify(exactly = 1) { accountRepository.save(walletUpdated) }
        verify(exactly = 1) { transactionRepository.save(transaction) }

    }

    @Test
    fun `increases balance from wallet when operation is deposit`() {
        val walletUpdated = AccountBuilder().apply { this.balance = BigDecimal.valueOf(300) }.build()
        val transaction = TransactionBuilder()
            .apply {
                this.type = TransactionType.DEPOSIT
                this.date = request.date
            }
            .build()
        every { accountRepository.getByAccountId(account.accountId) } returns account
        every { accountRepository.save(walletUpdated) } returns walletUpdated
        every { transactionRepository.save(transaction) } returns transaction

        transactionService.deposit(request)

        verify(exactly = 1) { accountRepository.save(walletUpdated) }
        verify(exactly = 1) { transactionRepository.save(transaction) }
    }

    @Test
    fun `increases balance from target and decreases balance from source when operation is deposit`() {
        val targetWallet = AccountBuilder()
            .apply {
                this.balance = BigDecimal.valueOf(200)
                this.accountId = "456"
            }
            .build()
        val sourceWalletUpdated = AccountBuilder()
            .apply { this.balance = BigDecimal.valueOf(100) }
            .build()
        val targetWalletUpdated = AccountBuilder()
            .apply {
                this.balance = BigDecimal.valueOf(300)
                this.accountId = "456"
            }
            .build()
        val transferRequest = TransactionTransferRequestBuilder().build()
        val transaction = TransactionBuilder()
            .apply {
                this.type = TransactionType.TRANSFER
                this.targetAccountId = targetWallet.accountId
                this.date = transferRequest.date
            }
            .build()
        every { accountRepository.getByAccountId(account.accountId) } returns account
        every { accountRepository.getByAccountId(targetWallet.accountId) } returns targetWallet
        every { accountRepository.save(sourceWalletUpdated) } returns sourceWalletUpdated
        every { accountRepository.save(targetWalletUpdated) } returns targetWalletUpdated
        every { transactionRepository.save(transaction) } returns transaction

        transactionService.transfer(transferRequest)

        verify(exactly = 1) { accountRepository.save(sourceWalletUpdated) }
        verify(exactly = 1) { accountRepository.save(targetWalletUpdated) }
        verify(exactly = 1) { transactionRepository.save(transaction) }
    }
}