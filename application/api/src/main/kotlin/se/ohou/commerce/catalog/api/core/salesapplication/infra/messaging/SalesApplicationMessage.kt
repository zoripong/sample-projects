package se.ohou.commerce.catalog.api.core.salesapplication.infra.messaging

// avro 로 교체되어야 함
object SalesApplicationMessage {
    data class Change(
        val id: String,
    )
}
