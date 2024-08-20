package com.picpay.wallet.configuration

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SnsConfiguration {
    @Value("\${cloud.aws.region}")
    private val region: String? = null

    @Value("\${cloud.aws.endpoint.uri}")
    private val host: String? = null

    @Value("\${cloud.aws.credentials.access-key}")
    private val accessKeyId: String? = null

    @Value("\${cloud.aws.credentials.secret-key}")
    private val secretAccessKey: String? = null

    @Bean
    fun snsClient(): AmazonSNS = AmazonSNSClientBuilder.standard()
        .withEndpointConfiguration(EndpointConfiguration(host, region))
        .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKeyId, secretAccessKey)))
        .build()
}