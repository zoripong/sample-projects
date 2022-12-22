package se.ohou.commerce.domain.goods

import java.time.ZonedDateTime

/**
 * 상품
 *
 * @property id 식별자
 * @property sellerId 판매자 id
 * @property name 상품명
 * @property imageUrl 대표이미지
 * @property subImageUrls 이미지 목록 (production_sub_images)
 * @property memoOption 메모 옵션
 * @property createdAt 생성 시각
 * @property updatedAt 수정 시각
 * @property hasMemoOption 메모옵션 존재 여부 (need_memo)
 */
class Goods(
    val id: Long?,
    val sellerId: Long,
    val name: String,
    val imageUrl: String,
    val subImageUrls: List<String>,
    val memoOption: MemoOption?,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
) {
    val hasMemoOption: Boolean
        get() = memoOption != null

    /**
     * 메모 옵션
     *
     * @property title 옵션 이름 (memo_title)
     * @property isEssential 필수 여부 (is_essential_memo)
     */
    data class MemoOption(
        val title: String,
        val isEssential: Boolean,
    )

    companion object {
        // NOTE: 생성과 관련된 도메인 함수 방식 #2
        fun create(
            sellerId: Long,
            name: String,
            imageUrl: String,
            subImageUrls: List<String>,
            memoOption: MemoOption?,
        ): Goods =
            Goods(
                id = null,
                sellerId = sellerId,
                name = name,
                imageUrl = imageUrl,
                subImageUrls = subImageUrls,
                memoOption = memoOption,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )
    }
}
