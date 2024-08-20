package com.picpay.wallet.builders

import com.picpay.wallet.infra.Transaction
import com.picpay.wallet.infra.Transaction.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

class TransactionBuilder {
    var id: Long = 0
    var type: TransactionType = TransactionType.DEPOSIT
    var amount: BigDecimal = BigDecimal.valueOf(100)
    var date: LocalDateTime = LocalDateTime.now()
    var sourceAccountId: String = "123"
    var targetAccountId: String? = null

    fun build() = Transaction(id, type, amount, date, sourceAccountId, targetAccountId)
}