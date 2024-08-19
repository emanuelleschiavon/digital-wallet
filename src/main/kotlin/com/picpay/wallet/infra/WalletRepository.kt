package com.picpay.wallet.infra

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository : JpaRepository<WalletEntity, Long> {
    fun getByAccountId(accountId: String): WalletEntity
}