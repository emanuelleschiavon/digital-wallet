package com.picpay.wallet.infra

import com.amazonaws.services.sns.AmazonSNS
import com.picpay.wallet.domain.Transaction
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TransactionPublisher(
    private val snsClient: AmazonSNS,
) {
    @Value("\${cloud.aws.topic.transaction-done}")
    lateinit var transactionDoneTopic: String

    private val logger = LoggerFactory.getLogger(TransactionPublisher::class.java)

    fun publish(transaction: Transaction): Transaction {
        snsClient.publish(transactionDoneTopic, transaction.toString())
        logger.info("Sent message: $transaction")
        return transaction
    }
}
