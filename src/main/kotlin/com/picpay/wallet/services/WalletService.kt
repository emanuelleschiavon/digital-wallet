package com.picpay.wallet.services

import com.picpay.wallet.infra.WalletRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class WalletService(
    private val walletRepository: WalletRepository,
) {

    @Transactional
    fun withdraw(accountId: String, value: BigDecimal) {
        return walletRepository.getByAccountId(accountId)
            .decreaseBalance(value)
            .let { walletRepository.save(it) }
        // emite evendo
    }

    @Transactional
    fun payment(accountId: String, value: BigDecimal) {
        return walletRepository.getByAccountId(accountId)
            .decreaseBalance(value)
            .let { walletRepository.save(it) }
        // emite evendo
    }

    @Transactional
    fun deposit(accountId: String, value: BigDecimal) {
        walletRepository.getByAccountId(accountId)
            .increaseBalance(value)
            .let { walletRepository.save(it) }
        // emite evendo
    }

    @Transactional
    fun transfer(sourceAccountId: String, targetAccountId: String, value: BigDecimal) {
        val sourceWallet = walletRepository.getByAccountId(sourceAccountId)
        val targetWallet = walletRepository.getByAccountId(targetAccountId)

        walletRepository.save(sourceWallet.decreaseBalance(value))
        walletRepository.save(targetWallet.increaseBalance(value))
        //emite evento
    }
}