package com.picpay.wallet.inbound

import com.picpay.wallet.services.WalletService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/wallets")
class WalletController(
    private val walletService: WalletService,
) {

    @PostMapping("/{accountId}/withdraw")
    fun withdraw(@PathVariable accountId: String, @RequestBody request: WalletRequest) {
        walletService.withdraw(accountId, request.value)
    }

    @PostMapping("/{accountId}/deposit")
    fun deposit(@PathVariable accountId: String, @RequestBody request: WalletRequest) {
        walletService.deposit(accountId, request.value)
    }

    @PostMapping("/{accountId}/payment")
    fun payment(@PathVariable accountId: String, @RequestBody request: WalletRequest) {
        walletService.payment(accountId, request.value)
    }

    @PostMapping("/{sourceAccountId}/transfer")
    fun transfer(@PathVariable sourceAccountId: String, @RequestBody request: TransferRequest) {
        walletService.transfer(sourceAccountId, request.targetAccountId, request.value)
    }
}