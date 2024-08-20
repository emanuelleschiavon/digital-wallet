package com.picpay.wallet.infra

import com.picpay.wallet.inbound.TransactionRequest
import com.picpay.wallet.inbound.TransactionTransferRequest
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
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
    val sourceAccountId: String,
    val targetAccountId: String? = null,
) {

    companion object {
        fun withDraw(request: TransactionRequest) =
            Transaction(
                type = TransactionType.WITHDRAW,
                amount = request.value,
                date = request.date,
                sourceAccountId = request.sourceAccountId,
            )

        fun deposit(request: TransactionRequest) =
            Transaction(
                type = TransactionType.DEPOSIT,
                amount = request.value,
                date = request.date,
                sourceAccountId = request.sourceAccountId,
            )

        fun payment(request: TransactionRequest) =
            Transaction(
                type = TransactionType.PAYMENT,
                amount = request.value,
                date = request.date,
                sourceAccountId = request.sourceAccountId,
            )

        fun transfer(request: TransactionTransferRequest) =
            Transaction(
                type = TransactionType.TRANSFER,
                amount = request.value,
                date = request.date,
                sourceAccountId = request.sourceAccountId,
                targetAccountId = request.targetAccountId,
            )
    }

    enum class TransactionType {
        WITHDRAW, DEPOSIT, PAYMENT, TRANSFER
    }
}
