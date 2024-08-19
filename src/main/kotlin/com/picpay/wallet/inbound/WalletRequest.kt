package com.picpay.wallet.inbound

import java.math.BigDecimal

data class WalletRequest (
    val value: BigDecimal
)

data class TransferRequest (
    val targetAccountId: String,
    val value: BigDecimal,
)