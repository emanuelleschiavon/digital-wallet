package com.picpay.wallet.domain.services

import com.picpay.wallet.inbound.TransactionRequest
import com.picpay.wallet.inbound.TransactionTransferRequest
import com.picpay.wallet.infra.AccountRepository
import com.picpay.wallet.domain.Transaction
import com.picpay.wallet.infra.TransactionPublisher
import com.picpay.wallet.infra.TransactionRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val transactionPublisher: TransactionPublisher,
) {

    @Transactional
    fun withdraw(transaction: TransactionRequest) =
        accountRepository.getReferenceById(transaction.sourceAccountId)
            .decreaseBalance(transaction.value)
            .let { accountRepository.save(it) }
            .let { Transaction.withdraw(it, transaction) }
            .let { transactionRepository.save(it) }
            .let { transactionPublisher.publish(it) }

    @Transactional
    fun payment(transaction: TransactionRequest) =
        accountRepository.getReferenceById(transaction.sourceAccountId)
            .decreaseBalance(transaction.value)
            .let { accountRepository.save(it) }
            .let { Transaction.payment(it, transaction) }
            .let { transactionRepository.save(it) }
            .let { transactionPublisher.publish(it) }

    @Transactional
    fun deposit(transaction: TransactionRequest) =
        accountRepository.getReferenceById(transaction.sourceAccountId)
            .increaseBalance(transaction.value)
            .let { accountRepository.save(it) }
            .let { Transaction.deposit(it, transaction) }
            .let { transactionRepository.save(it) }
            .let { transactionPublisher.publish(it) }

    @Transactional
    fun transfer(transaction: TransactionTransferRequest): Transaction {
        val sourceAccount = accountRepository.getReferenceById(transaction.sourceAccountId)
        val targetAccount = accountRepository.getReferenceById(transaction.targetAccountId)

        accountRepository.save(sourceAccount.decreaseBalance(transaction.value))
        accountRepository.save(targetAccount.increaseBalance(transaction.value))

        return Transaction.transfer(sourceAccount, transaction)
            .let { transactionRepository.save(it) }
            .let { transactionPublisher.publish(it) }
    }
}