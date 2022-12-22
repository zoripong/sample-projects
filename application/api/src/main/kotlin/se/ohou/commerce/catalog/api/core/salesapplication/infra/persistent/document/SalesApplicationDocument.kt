package se.ohou.commerce.catalog.api.core.salesapplication.infra.persistent.document

import se.ohou.commerce.domain.salesapplication.SalesApplication

@Document("SalesApplication")
class SalesApplicationDocument(
    @MongoId(value = FieldType.OBJECT_ID)
    val id: String? = null,
) {
    fun toDomain(): SalesApplication = TODO()
}

fun SalesApplication.toDocument(): SalesApplicationDocument = TODO()
