package com.picpay.wallet.builders

import com.picpay.wallet.inbound.TransactionRequest
import com.picpay.wallet.inbound.TransactionTransferRequest
import java.math.BigDecimal
import java.time.LocalDateTime

class TransactionRequestBuilder {
    var sourceAccountId: String = "123"
    var value: BigDecimal = BigDecimal.valueOf(100)
    var date: LocalDateTime = LocalDateTime.now()

    fun build() = TransactionRequest(sourceAccountId, value, date)
}

class TransactionTransferRequestBuilder {
    var sourceAccountId: String = "123"
    var targetAccountId: String = "456"
    var value: BigDecimal = BigDecimal.valueOf(100)
    var date: LocalDateTime = LocalDateTime.now()

    fun build() = TransactionTransferRequest(sourceAccountId, targetAccountId, value, date)
}
