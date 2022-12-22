package se.ohou.commerce.catalog.api.core.salesapplication.usecase

import org.springframework.stereotype.Service
import se.ohou.commerce.catalog.api.common.support.logging.Loggable
import se.ohou.commerce.domain.salesapplication.SalesApplication

// Service 는 1개 이상의 도메인 함수를 "조합"해서 사용하는 곳
@Service
class PartnerActionService(
    private val salesApplicationRepository: SalesApplicationRepository,
    private val salesApplicationProducer: SalesApplicationProducer,
) : Loggable {

    // @Transactional // sample project에 dependency가 없어서 우선 주석처리
    fun approve(command: RequestApproval.Command): RequestApproval.Result {
        val salesApplication = salesApplicationRepository.get(command.salesApplicationId)
        val approvedSalesApplication = salesApplication
            .requestApproval(sellerId = command.sellerId)
            .let { salesApplicationRepository.save(it) }

        salesApplicationProducer.notifyUpdate(approvedSalesApplication)
        return RequestApproval.Result(salesApplication = approvedSalesApplication)
    }

    interface SalesApplicationRepository {
        fun get(salesApplicationId: Long): SalesApplication
        fun save(salesApplication: SalesApplication): SalesApplication
    }

    interface SalesApplicationProducer {
        fun notifyUpdate(salesApplication: SalesApplication)
    }
}