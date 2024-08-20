package com.picpay.wallet.inbound

import com.picpay.wallet.domain.services.TransactionService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/transactions")
class TransactionController(
    private val transactionService: TransactionService,
) {

    @PostMapping("/withdraw")
    fun withdraw(@RequestBody request: TransactionRequest) =
        transactionService.withdraw(request)

    @PostMapping("/deposit")
    fun deposit(@RequestBody request: TransactionRequest) =
        transactionService.deposit(request)

    @PostMapping("/payment")
    fun payment(@RequestBody request: TransactionRequest) =
        transactionService.payment(request)

    @PostMapping("/transfer")
    fun transfer(@RequestBody request: TransactionTransferRequest) =
        transactionService.transfer(request)
}