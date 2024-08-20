package com.picpay.wallet.builders

import com.picpay.wallet.infra.Account
import java.math.BigDecimal

class AccountBuilder {
    var id: Long = 0
    var accountId: String = "123"
    var balance: BigDecimal = BigDecimal.valueOf(20000)

    fun build() = Account(id, accountId, balance)
}