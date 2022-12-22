package se.ohou.commerce.catalog.api.common.support.messaging

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.kafka.core.KafkaProducerException
import org.springframework.kafka.core.KafkaSendCallback
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult

abstract class KafkaProducer<P>(private val kafkaTemplate: KafkaTemplate<String, String>) : Producer<String, P> {
    abstract val topicName: String

    abstract val defaultCallback: KafkaSendCallback<String, String>

    private fun serializeMessage(type: String, payload: P): String {
        val message = this.buildMessage(type, payload)
        return objectMapper.writeValueAsString(message)
    }
    override fun send(type: String, payload: P) {
        kafkaTemplate
            .send(topicName, serializeMessage(type, payload))
            .addCallback(defaultCallback)
    }

    override fun send(
        type: String,
        payload: P,
        successCallback: () -> Unit,
        failedCallback: (exception: Exception) -> Unit
    ) {
        kafkaTemplate
            .send(topicName, serializeMessage(type, payload))
            .addCallback(
                object : KafkaSendCallback<String, String> {
                    override fun onSuccess(result: SendResult<String, String>?) {
                        successCallback()
                    }

                    override fun onFailure(ex: KafkaProducerException) {
                        failedCallback(ex)
                    }
                }
            )
    }

    override fun send(type: String, key: String, payload: P) {
        kafkaTemplate
            .send(topicName, key, serializeMessage(type, payload))
            .addCallback(defaultCallback)
    }

    override fun send(
        type: String,
        key: String,
        payload: P,
        successCallback: () -> Unit,
        failedCallback: (exception: Exception) -> Unit
    ) {
        kafkaTemplate
            .send(topicName, key, serializeMessage(type, payload))
            .addCallback(
                object : KafkaSendCallback<String, String> {
                    override fun onSuccess(result: SendResult<String, String>?) {
                        successCallback()
                    }

                    override fun onFailure(ex: KafkaProducerException) {
                        failedCallback(ex)
                    }
                }
            )
    }

    companion object {
        private val objectMapper = jacksonObjectMapper()
            .apply {
                registerModule(JavaTimeModule())
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            }
    }
}
