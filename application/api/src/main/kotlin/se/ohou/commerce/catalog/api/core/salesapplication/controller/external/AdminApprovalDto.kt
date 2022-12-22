package se.ohou.commerce.catalog.api.core.salesapplication.controller.external

// NOTE: controller 가 받는건 dto
object AdminApprovalDto {
    data class Request(
        val message: String
    )

    data class Response(
        val goodsId: Long,
    )
}