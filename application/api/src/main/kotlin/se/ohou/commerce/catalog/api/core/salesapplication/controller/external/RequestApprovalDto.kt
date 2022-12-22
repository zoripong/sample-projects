package se.ohou.commerce.catalog.api.core.salesapplication.controller.external

// NOTE: controller 가 받는건 dto
object RequestApprovalDto {
    data class Response(
        val salesApplicationId: Long,
    )
}