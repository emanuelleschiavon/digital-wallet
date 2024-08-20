package com.picpay.wallet.infra

import com.amazonaws.services.sns.AmazonSNS
import com.picpay.wallet.domain.Transaction
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TransactionPublisher(
    private val snsClient: AmazonSNS,
) {
    @Value("\${cloud.aws.topic.transaction-done}")
    lateinit var transactionDoneTopic: String
    fun publish(transaction: Transaction): Transaction {
        snsClient.publish(transactionDoneTopic, transaction.toString())
        return transaction
    }
}
