package com.picpay.wallet.infra

import java.math.BigDecimal

class AccountEntityBuilder {
    var id: Long = 0
    var accountId: String = "123"
    var balance: BigDecimal = BigDecimal.valueOf(20000L)

    fun build() = AccountEntity(id, accountId, balance)
}