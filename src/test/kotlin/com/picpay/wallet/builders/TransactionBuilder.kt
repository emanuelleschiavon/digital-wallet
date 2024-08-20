package com.picpay.wallet.builders

import com.picpay.wallet.domain.Account
import com.picpay.wallet.domain.Transaction
import com.picpay.wallet.domain.Transaction.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

class TransactionBuilder {
    var id: Long = 0
    var type: TransactionType = TransactionType.DEPOSIT
    var amount: BigDecimal = BigDecimal.valueOf(100)
    var date: LocalDateTime = LocalDateTime.now()
    var targetAccountId: String? = null
    var sourceAccount: Account = AccountBuilder().apply { this.accountId = "123" }.build()

    fun build() = Transaction(id, type, amount, date, targetAccountId, sourceAccount)
}