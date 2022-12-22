package se.ohou.commerce.catalog.api.core.goods.controller.internal

object GoodsCreationDto {
    data class Request(
        val name: String,
    )

    data class Response(
        val id: Long,
    )
}