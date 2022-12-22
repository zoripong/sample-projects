package se.ohou.commerce.catalog.api.core.salesapplication.infra.persistent.repository

import org.springframework.stereotype.Repository
import se.ohou.commerce.catalog.api.core.salesapplication.infra.persistent.document.toDocument
import se.ohou.commerce.domain.salesapplication.SalesApplication

@Repository
class SalesApplicationRepositoryImpl(
    private val salesApplicationMongoRepository: SalesApplicationMongoRepository,
) : SalesApplicationRepository {
    override fun get(salesApplicationId: Long): SalesApplication {
        return salesApplicationMongoRepository
            .findById(salesApplicationId)
            .get()
            .toDomain()
    }

    override fun save(salesApplication: SalesApplication): SalesApplication {
        return salesApplicationMongoRepository
            .save(salesApplication.toDocument())
            .toDomain()
    }
}