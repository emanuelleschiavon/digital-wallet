package com.picpay.wallet.infra

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<AccountEntity, Long> {
    fun getByAccountId(accountId: String): AccountEntity
}