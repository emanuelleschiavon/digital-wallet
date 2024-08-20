package com.picpay.wallet.builders

import com.picpay.wallet.domain.Account
import java.math.BigDecimal

class AccountBuilder {
    var accountId: String = "123"
    var balance: BigDecimal = BigDecimal.valueOf(20000)

    fun build() = Account(accountId, balance)
}