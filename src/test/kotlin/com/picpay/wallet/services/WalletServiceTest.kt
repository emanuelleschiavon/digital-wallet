package com.picpay.wallet.services

import com.picpay.wallet.infra.WalletEntityBuilder
import com.picpay.wallet.infra.WalletRepository
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
    private lateinit var walletRepository: WalletRepository

    @Test
    fun `decreases balance from wallet when operation is withdraw`() {
        val wallet = WalletEntityBuilder().apply { this.balance = BigDecimal.valueOf(200) }.build()
        val walletUpdated = WalletEntityBuilder().apply { this.balance = BigDecimal.valueOf(100) }.build()
        every {
            walletRepository.getByAccountId(wallet.accountId)
        } returns wallet
        every {
            walletRepository.save(walletUpdated)
        } returns walletUpdated

        walletService.withdraw(wallet.accountId, BigDecimal.valueOf(100))

        verify(exactly = 1) {
            walletRepository.save(walletUpdated)
        }
    }

    @Test
    fun `decreases balance from wallet when operation is payment`() {
        val wallet = WalletEntityBuilder().apply { this.balance = BigDecimal.valueOf(200) }.build()
        val walletUpdated = WalletEntityBuilder().apply { this.balance = BigDecimal.valueOf(100) }.build()
        every {
            walletRepository.getByAccountId(wallet.accountId)
        } returns wallet
        every {
            walletRepository.save(walletUpdated)
        } returns walletUpdated

        walletService.payment(wallet.accountId, BigDecimal.valueOf(100))

        verify(exactly = 1) {
            walletRepository.save(walletUpdated)
        }
    }

    @Test
    fun `increases balance from wallet when operation is deposit`() {
        val wallet = WalletEntityBuilder().apply { this.balance = BigDecimal.valueOf(200) }.build()
        val walletUpdated = WalletEntityBuilder().apply { this.balance = BigDecimal.valueOf(300) }.build()
        every {
            walletRepository.getByAccountId(wallet.accountId)
        } returns wallet
        every {
            walletRepository.save(walletUpdated)
        } returns walletUpdated

        walletService.deposit(wallet.accountId, BigDecimal.valueOf(100))

        verify(exactly = 1) {
            walletRepository.save(walletUpdated)
        }
    }

    @Test
    fun `increases balance from target and decreases balance from source when operation is deposit`() {
        val sourceWallet = WalletEntityBuilder()
            .apply { this.balance = BigDecimal.valueOf(200) }
            .build()
        val targetWallet = WalletEntityBuilder()
            .apply { this.balance = BigDecimal.valueOf(200) }
            .build()
        val sourceWalletUpdated = WalletEntityBuilder()
            .apply { this.balance = BigDecimal.valueOf(100) }
            .build()
        val targetWalletUpdated = WalletEntityBuilder()
            .apply { this.balance = BigDecimal.valueOf(300) }
            .build()
        every {
            walletRepository.getByAccountId(sourceWallet.accountId)
        } returns sourceWallet
        every {
            walletRepository.getByAccountId(targetWallet.accountId)
        } returns targetWallet
        every {
            walletRepository.save(sourceWalletUpdated)
        } returns sourceWalletUpdated
        every {
            walletRepository.save(targetWalletUpdated)
        } returns targetWalletUpdated

        walletService.transfer(sourceWallet.accountId, targetWallet.accountId, BigDecimal.valueOf(100))

        verify(exactly = 1) {
            walletRepository.save(sourceWalletUpdated)
        }
        verify(exactly = 1) {
            walletRepository.save(targetWalletUpdated)
        }
    }
}