package com.picpay.wallet.services

import com.picpay.wallet.infra.AccountRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class WalletService(
    private val accountRepository: AccountRepository,
) {

    @Transactional
    fun withdraw(accountId: String, value: BigDecimal) {
        return accountRepository.getByAccountId(accountId)
            .decreaseBalance(value)
            .let { accountRepository.save(it) }
        // emite evendo
    }

    @Transactional
    fun payment(accountId: String, value: BigDecimal) {
        return accountRepository.getByAccountId(accountId)
            .decreaseBalance(value)
            .let { accountRepository.save(it) }
        // emite evendo
    }

    @Transactional
    fun deposit(accountId: String, value: BigDecimal) {
        accountRepository.getByAccountId(accountId)
            .increaseBalance(value)
            .let { accountRepository.save(it) }
        // emite evendo
    }

    @Transactional
    fun transfer(sourceAccountId: String, targetAccountId: String, value: BigDecimal) {
        val sourceWallet = accountRepository.getByAccountId(sourceAccountId)
        val targetWallet = accountRepository.getByAccountId(targetAccountId)

        accountRepository.save(sourceWallet.decreaseBalance(value))
        accountRepository.save(targetWallet.increaseBalance(value))
        //emite evento
    }
}