package com.picpay.wallet.infra

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity(name = "wallets")
data class WalletEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    val accountId: String,
    val balance: BigDecimal,
) {

    fun decreaseBalance(value: BigDecimal): WalletEntity {
        return if (balance < value) {
            throw Exception("Saldo Insuficiente")
        } else {
            this.copy(balance = balance - value)
        }
    }

    fun increaseBalance(value: BigDecimal) = this.copy(balance = balance + value)

}