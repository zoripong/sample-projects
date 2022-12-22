package se.ohou.commerce.catalog.api.core.salesapplication.infra.gateway

import org.springframework.stereotype.Component
import se.ohou.commerce.catalog.api.core.goods.controller.internal.GoodsCreateInternalController
import se.ohou.commerce.catalog.api.core.goods.controller.internal.GoodsCreationDto
import se.ohou.commerce.domain.salesapplication.SalesContent

@Component
class GoodsGatewayImpl : GoodsGateway {
    override fun create(salesContent: SalesContent): Long {
        val result = GoodsCreateInternalController.create(
            GoodsCreationDto.Request(name = salesContent.goods.name)
        )
        return result.id
    }
}