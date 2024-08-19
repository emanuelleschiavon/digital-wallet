package com.picpay.wallet.inbound

import com.picpay.wallet.infra.WalletEntityBuilder
import com.picpay.wallet.services.WalletService
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
class WalletControllerTest {

    @InjectMockKs
    private lateinit var walletController: WalletController

    @MockK
    private lateinit var walletService: WalletService

    private val amount = BigDecimal.valueOf(100)
    private val request = WalletRequest(amount)

    @Test
    fun `withdraws money`() {
        val wallet = WalletEntityBuilder().build()
        every {
            walletService.withdraw(wallet.accountId, amount)
        } just Runs

        walletController.withdraw(wallet.accountId, request)

        verify(exactly = 1) {
            walletService.withdraw(wallet.accountId, amount)
        }
    }

    @Test
    fun `deposits money`() {
        val wallet = WalletEntityBuilder().build()
        every {
            walletService.deposit(wallet.accountId, amount)
        } just Runs

        walletController.deposit(wallet.accountId, request)

        verify(exactly = 1) {
            walletService.deposit(wallet.accountId, amount)
        }
    }

    @Test
    fun `pay money`() {
        val wallet = WalletEntityBuilder().build()
        every {
            walletService.payment(wallet.accountId, amount)
        } just Runs

        walletController.payment(wallet.accountId, request)

        verify(exactly = 1) {
            walletService.payment(wallet.accountId, amount)
        }
    }

    @Test
    fun `transfer money`() {
        val sourceWallet = WalletEntityBuilder().build()
        val targetWallet = WalletEntityBuilder().build()
        val request = TransferRequest(targetWallet.accountId, amount)
        every {
            walletService.transfer(sourceWallet.accountId, targetWallet.accountId, amount)
        } just Runs

        walletController.transfer(sourceWallet.accountId, request)

        verify(exactly = 1) {
            walletService.transfer(sourceWallet.accountId, targetWallet.accountId, amount)
        }
    }
}