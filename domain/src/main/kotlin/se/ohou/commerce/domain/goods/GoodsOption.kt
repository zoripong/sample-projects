package se.ohou.commerce.domain.goods

import java.time.ZonedDateTime

/**
 * 상품 옵션
 *
 * @property id 식별자
 * @property goodsId 상품 식별자
 * @property optionValues 옵션 값
 * @property imageUrls 옵션 이미지
 * @property createdAt 옵션 생성 시각
 * @property updatedAt 옵션 마지막 수정 시각
 */
class GoodsOption(
    val id: Long?,
    val goodsId: Long,
    val optionValues: List<String>,
    val imageUrls: List<String>,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
) {
    // NOTE: 생성과 관련된 도메인 함수 방식 #2
    constructor(goodsId: Long, optionValues: List<String>, imageUrls: List<String>) : this(
        id = null,
        goodsId = goodsId,
        optionValues = optionValues,
        imageUrls = imageUrls,
        createdAt = ZonedDateTime.now(),
        updatedAt = ZonedDateTime.now()
    )
}