package com.picpay.wallet.services

import com.picpay.wallet.inbound.TransactionRequest
import com.picpay.wallet.inbound.TransactionTransferRequest
import com.picpay.wallet.infra.AccountRepository
import com.picpay.wallet.infra.Transaction
import com.picpay.wallet.infra.TransactionRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
) {

    @Transactional
    fun withdraw(transaction: TransactionRequest) =
        accountRepository.getByAccountId(transaction.sourceAccountId)
            .decreaseBalance(transaction.value)
            .let { accountRepository.save(it) }
            .let { Transaction.withDraw(transaction) }
            .let { transactionRepository.save(it) }
    // emite evendo

    @Transactional
    fun payment(transaction: TransactionRequest) =
        accountRepository.getByAccountId(transaction.sourceAccountId)
            .decreaseBalance(transaction.value)
            .let { accountRepository.save(it) }
            .let { Transaction.payment(transaction) }
            .let { transactionRepository.save(it) }
    // emite evendo

    @Transactional
    fun deposit(transaction: TransactionRequest) =
        accountRepository.getByAccountId(transaction.sourceAccountId)
            .increaseBalance(transaction.value)
            .let { accountRepository.save(it) }
            .let { Transaction.deposit(transaction) }
            .let { transactionRepository.save(it) }
    // emite evendo

    @Transactional
    fun transfer(transaction: TransactionTransferRequest): Transaction {
        val sourceAccount = accountRepository.getByAccountId(transaction.sourceAccountId)
        val targetAccount = accountRepository.getByAccountId(transaction.targetAccountId)

        accountRepository.save(sourceAccount.decreaseBalance(transaction.value))
        accountRepository.save(targetAccount.increaseBalance(transaction.value))

        return Transaction.transfer(transaction)
            .let { transactionRepository.save(it) }
        //emite evento
    }
}