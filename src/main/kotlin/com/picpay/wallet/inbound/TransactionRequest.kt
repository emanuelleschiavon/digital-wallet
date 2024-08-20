package com.picpay.wallet.inbound

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionRequest(
    val sourceAccountId: String,
    val value: BigDecimal,
    val date: LocalDateTime,
)

data class TransactionTransferRequest(
    val sourceAccountId: String,
    val targetAccountId: String,
    val value: BigDecimal,
    val date: LocalDateTime,
)
