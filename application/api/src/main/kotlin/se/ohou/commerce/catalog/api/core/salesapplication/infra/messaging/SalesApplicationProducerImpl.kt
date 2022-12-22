package se.ohou.commerce.catalog.api.core.salesapplication.infra.messaging

import org.springframework.stereotype.Component
import se.ohou.commerce.domain.salesapplication.SalesApplication

@Component
class SalesApplicationProducerImpl(
    private val salesApplicationKafkaProducer: SalesApplicationKafkaProducer,
) : SalesApplicationProducer {
    override fun notifyUpdate(salesApplication: SalesApplication) {
        salesApplicationKafkaProducer.send(
            "UPDATE",
                SalesApplicationMessage.Change(
                    id = requireNotNull(salesApplication.id).toString(), // NOTE 다른 필드들 추가 필요
                )
        )
    }
}