package se.ohou.commerce.catalog.api.core.salesapplication.infra.persistent.repository

import org.springframework.data.mongodb.repository.MongoRepository
import se.ohou.commerce.catalog.api.core.salesapplication.infra.persistent.document.SalesApplicationDocument

interface SalesApplicationMongoRepository : MongoRepository<SalesApplicationDocument, Long>