package se.ohou.commerce.catalog.api.core.salesapplication.infra.messaging

import org.springframework.kafka.core.KafkaProducerException
import org.springframework.kafka.core.KafkaSendCallback
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import se.ohou.commerce.catalog.api.common.support.logging.Loggable
import se.ohou.commerce.catalog.api.common.support.messaging.KafkaProducer
import se.ohou.commerce.catalog.api.common.support.messaging.Producer

@Component
class SalesApplicationKafkaProducer(
    kafkaTemplate: KafkaTemplate<String, String>,
) : KafkaProducer<SalesApplicationMessage.Change>(kafkaTemplate),
    Producer<String, SalesApplicationMessage.Change>,
    Loggable {

    override val topicName: String = "sales-applications"

    override val defaultCallback: KafkaSendCallback<String, String> =
        object : KafkaSendCallback<String, String> {
            override fun onSuccess(result: SendResult<String, String>?) {
                result?.producerRecord?.value()?.let {
                    log.info("sales-application change event is produced: $it")
                }
            }
            override fun onFailure(ex: KafkaProducerException) {
                log.error("failed to produce sales-application change event: ${ex.stackTraceToString()}")
            }
        }

    override fun buildMessage(
        type: String,
        payload: SalesApplicationMessage.Change
    ): Producer.Message<SalesApplicationMessage.Change> =
        Producer.Message(
            type = type,
            version = "0.0.1",
            body = payload,
        )
}
