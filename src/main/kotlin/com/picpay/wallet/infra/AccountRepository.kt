package com.picpay.wallet.infra

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    fun getByAccountId(accountId: String): Account
}