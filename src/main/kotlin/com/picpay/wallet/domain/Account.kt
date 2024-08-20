package com.picpay.wallet.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.io.Serializable
import java.math.BigDecimal

@Entity(name = "accounts")
data class Account(
    @Id val accountId: String,
    val balance: BigDecimal,
) : Serializable {

    fun decreaseBalance(value: BigDecimal): Account {
        return if (balance < value) {
            throw Exception("Saldo Insuficiente")
        } else {
            this.copy(balance = balance - value)
        }
    }

    fun increaseBalance(value: BigDecimal) = this.copy(balance = balance + value)

}