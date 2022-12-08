package se.ohou.commerce.consumer.sample.adapter.input

import kotlinx.coroutines.runBlocking
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import se.ohou.commerce.consumer.config.logging.Logging

@Component
class SampleListener {
    @KafkaListener(
        topics = ["\${spring.kafka.topics.sample}"],
        groupId = "\${spring.application.name}",
    )
    fun listen(@Payload payload: String) = runBlocking {
        log.info("Receiving event: {}", payload)
    }

    companion object : Logging
}
