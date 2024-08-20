package com.picpay.wallet.services

import com.picpay.wallet.infra.AccountEntityBuilder
import com.picpay.wallet.infra.AccountRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
class WalletServiceTest {

    @InjectMockKs
    private lateinit var walletService: WalletService

    @MockK
    private lateinit var accountRepository: AccountRepository

    @Test
    fun `decreases balance from wallet when operation is withdraw`() {
        val wallet = AccountEntityBuilder().apply { this.balance = BigDecimal.valueOf(200) }.build()
        val walletUpdated = AccountEntityBuilder().apply { this.balance = BigDecimal.valueOf(100) }.build()
        every {
            accountRepository.getByAccountId(wallet.accountId)
        } returns wallet
        every {
            accountRepository.save(walletUpdated)
        } returns walletUpdated

        walletService.withdraw(wallet.accountId, BigDecimal.valueOf(100))

        verify(exactly = 1) {
            accountRepository.save(walletUpdated)
        }
    }

    @Test
    fun `decreases balance from wallet when operation is payment`() {
        val wallet = AccountEntityBuilder().apply { this.balance = BigDecimal.valueOf(200) }.build()
        val walletUpdated = AccountEntityBuilder().apply { this.balance = BigDecimal.valueOf(100) }.build()
        every {
            accountRepository.getByAccountId(wallet.accountId)
        } returns wallet
        every {
            accountRepository.save(walletUpdated)
        } returns walletUpdated

        walletService.payment(wallet.accountId, BigDecimal.valueOf(100))

        verify(exactly = 1) {
            accountRepository.save(walletUpdated)
        }
    }

    @Test
    fun `increases balance from wallet when operation is deposit`() {
        val wallet = AccountEntityBuilder().apply { this.balance = BigDecimal.valueOf(200) }.build()
        val walletUpdated = AccountEntityBuilder().apply { this.balance = BigDecimal.valueOf(300) }.build()
        every {
            accountRepository.getByAccountId(wallet.accountId)
        } returns wallet
        every {
            accountRepository.save(walletUpdated)
        } returns walletUpdated

        walletService.deposit(wallet.accountId, BigDecimal.valueOf(100))

        verify(exactly = 1) {
            accountRepository.save(walletUpdated)
        }
    }

    @Test
    fun `increases balance from target and decreases balance from source when operation is deposit`() {
        val sourceWallet = AccountEntityBuilder()
            .apply { this.balance = BigDecimal.valueOf(200) }
            .build()
        val targetWallet = AccountEntityBuilder()
            .apply { this.balance = BigDecimal.valueOf(200) }
            .build()
        val sourceWalletUpdated = AccountEntityBuilder()
            .apply { this.balance = BigDecimal.valueOf(100) }
            .build()
        val targetWalletUpdated = AccountEntityBuilder()
            .apply { this.balance = BigDecimal.valueOf(300) }
            .build()
        every {
            accountRepository.getByAccountId(sourceWallet.accountId)
        } returns sourceWallet
        every {
            accountRepository.getByAccountId(targetWallet.accountId)
        } returns targetWallet
        every {
            accountRepository.save(sourceWalletUpdated)
        } returns sourceWalletUpdated
        every {
            accountRepository.save(targetWalletUpdated)
        } returns targetWalletUpdated

        walletService.transfer(sourceWallet.accountId, targetWallet.accountId, BigDecimal.valueOf(100))

        verify(exactly = 1) {
            accountRepository.save(sourceWalletUpdated)
        }
        verify(exactly = 1) {
            accountRepository.save(targetWalletUpdated)
        }
    }
}