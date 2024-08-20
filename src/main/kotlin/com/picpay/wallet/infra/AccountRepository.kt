package com.picpay.wallet.infra

import com.picpay.wallet.domain.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, String>