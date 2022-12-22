package se.ohou.commerce.catalog.api.core.salesapplication.usecase

import se.ohou.commerce.domain.goods.Goods // TODO: 고민1) 여기서 goods로 접근하는게 맞을까?
import se.ohou.commerce.domain.salesapplication.SalesApplication

// NOTE usecase 가 받는건 command
// API request / response dto 와는 독립적임
object AdminApproval {
    /**
     * @property salesApplicationId 상품판매신청서 id
     * @property message 승인 문구
     * @property adminId 승인하는 어드민
     */
    data class Command(
        val salesApplicationId: Long,
        val message: String,
        val adminId: Int,
    )

    /**
     * @property salesApplication 승인된 상품판매신청서
     */
    data class Result(
        val salesApplication: SalesApplication,
    )
}