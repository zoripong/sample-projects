package se.ohou.commerce.catalog.api.core.salesapplication.usecase

import org.springframework.stereotype.Service
import se.ohou.commerce.catalog.api.common.support.logging.Loggable
import se.ohou.commerce.domain.salesapplication.SalesApplication
import se.ohou.commerce.domain.salesapplication.SalesContent

// Service 는 1개 이상의 도메인 함수를 "조합"해서 사용하는 곳
@Service
class AdminActionService(
    private val salesApplicationRepository: SalesApplicationRepository,
    private val goodsGateway: GoodsGateway,
    private val salesApplicationProducer: SalesApplicationProducer,
) : Loggable {

    // @Transactional // sample project에 dependency가 없어서 우선 주석처리
    fun approve(command: AdminApproval.Command): AdminApproval.Result {
        val salesApplication = salesApplicationRepository.get(command.salesApplicationId)
        val goodsId = goodsGateway.create(salesApplication.content)
        val approvedSalesApplication = salesApplication
            .approve(
                goodsId = goodsId,
                adminId = command.adminId,
                message = command.message,
            )
            .let { salesApplicationRepository.save(it) }

        salesApplicationProducer.notifyUpdate(approvedSalesApplication)
        return AdminApproval.Result(salesApplication = approvedSalesApplication)
    }

    interface SalesApplicationRepository {
        fun get(salesApplicationId: Long): SalesApplication
        fun save(salesApplication: SalesApplication): SalesApplication
    }

    interface GoodsGateway {
        fun create(salesContent: SalesContent): Long
    }

    interface SalesApplicationProducer {
        fun notifyUpdate(salesApplication: SalesApplication)
    }
}