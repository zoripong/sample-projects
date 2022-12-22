package se.ohou.commerce.domain.salesapplication

import java.time.LocalDateTime
import java.time.ZonedDateTime

/**
 * 상품 판매신청서
 *
 * @property id 식별자
 * @property goodsId 생성된 상품
 * @property content 판매할 상품 정보
 * @property status 판매신청서 상태
 * @property requestedAt 판매 신청 시각
 * @property createdBy 판매자
 * @property createdAt 생성 시각
 * @property updatedBy 마지막 수정자
 */
class SalesApplication(
    val id: Long? = null,
    val goodsId: Long? = null,
    val content: SalesContent,
    val status: Status = Status.TEMPORARY,
    val requestedAt: ZonedDateTime?,
    val createdBy: Int,
    val createdAt: ZonedDateTime? = null,
    val updatedBy: Int,
) {

    fun requestApproval(sellerId: Int): SalesApplication {
        check(status == Status.TEMPORARY)
        return SalesApplication(
            id = id,
            content = content,
            createdBy = createdBy,
            createdAt = createdAt,
            status = Status.WAITING,
            requestedAt = ZonedDateTime.now(),
            updatedBy = sellerId,
        )

    }
    /**
     * 상품 판매신청서를 승인합니다.
     * 생성된 상품과 연결됩니다.
     */
    fun approve(goodsId: Long, adminId: Int, message: String): SalesApplication {
        check(status == Status.WAITING)
        return SalesApplication(
            id = id,
            content = content,
            requestedAt = requestedAt,
            createdAt = createdAt,
            createdBy = createdBy,
            status = Status.APPROVED,
            updatedBy = adminId,
            goodsId = goodsId,
        )
    }

    /**
     * 상품판매신청서 상태
     *
     * @property TEMPORARY 임시저장
     * @property WAITING 승인 대기
     * @property APPROVED 승인
     * @property HOLDING 보류
     * @property REJECTED 반려
     */
    enum class Status {
        TEMPORARY,
        WAITING,
        APPROVED,
        HOLDING,
        REJECTED
    }
}