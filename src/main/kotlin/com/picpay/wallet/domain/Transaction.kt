package com.picpay.wallet.domain

import com.picpay.wallet.inbound.TransactionRequest
import com.picpay.wallet.inbound.TransactionTransferRequest
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,
    val type: TransactionType,
    val amount: BigDecimal,
    val date: LocalDateTime,
    val targetAccountId: String? = null,
    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false)
    val sourceAccount: Account,
) {

    companion object {
        fun withdraw(account: Account, request: TransactionRequest) =
            Transaction(
                type = TransactionType.WITHDRAW,
                amount = request.value,
                date = request.date,
                sourceAccount = account,
            )

        fun deposit(account: Account, request: TransactionRequest) =
            Transaction(
                type = TransactionType.DEPOSIT,
                amount = request.value,
                date = request.date,
                sourceAccount = account,
            )

        fun payment(account: Account, request: TransactionRequest) =
            Transaction(
                type = TransactionType.PAYMENT,
                amount = request.value,
                date = request.date,
                sourceAccount = account,
            )

        fun transfer(sourceAccount: Account, request: TransactionTransferRequest) =
            Transaction(
                type = TransactionType.TRANSFER,
                amount = request.value,
                date = request.date,
                sourceAccount = sourceAccount,
                targetAccountId = request.targetAccountId,
            )
    }

    enum class TransactionType {
        WITHDRAW, DEPOSIT, PAYMENT, TRANSFER
    }
}
