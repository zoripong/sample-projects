package se.ohou.commerce.catalog.api.core.goods.controller.internal

object GoodsCreateInternalController {
    fun create(data: GoodsCreationDto.Request): GoodsCreationDto.Response {
        // 상품 저장
        return GoodsCreationDto.Response(id = 1)
    }

}