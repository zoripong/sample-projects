/**
 * 판매중 상품 수정 검수 요청 내역
 *
 * @property id 식별자
 * @property productId 수정하는 상품 Id
 * @property status 수정 검수 상태
 * @property snapshot 변경 전 상품 정보
 * @property modified 변경을 원하는 필드 (변경되는 데이터만 관리)
 * @property createdBy 검수 요청한 사용자
 * @property createdAt 검수 요청 시각
 * @property memo 검수 메모
 * @property completedBy 검수한 사용자
 * @property completedAt 검수 완료 시각
 * @property preInspection 사전 검수를 위한 변경 Diff (변경되는 데이터만 관리)
 * @property postInspection 사후 검수를 위한 변경 Diff (변경되는 데이터만 관리)
 * @property readyToPostInspection 사후 검수 진행 필요 여부
 * @property modifiedPreInspectionFields 수정된 사전 검수 항목 리스트
 */
data class Inspection(
    val id: String?,
    val productId: Int,
    val status: InspectionStatus,
    val snapshot: SalesContentSnapshot,
    val modified: SalesContentDiff,
    val createdBy: Account,
    val createdAt: LocalDateTime,
    val memo: String?,
    val completedBy: Account?,
    val completedAt: LocalDateTime?,
) {
    val isFinished: Boolean
        get() = status != InspectionStatus.WAITING

    val readyToPostInspection: Boolean
        get() = postInspection.isNotEmpty() && status in setOf(InspectionStatus.APPROVED, InspectionStatus.IMMEDIATELY)

    val modifiedPreInspectionFields: List<InspectionField>
        get() {
            if (preInspection.isEmpty()) return emptyList()
            return InspectionField.values().toList()
                .filter { inspectionField ->
                    inspectionField.step == InspectionTarget.Step.PRE &&
                            preInspection.any { inspectionField.isTarget(it) }
                }
        }

    fun approve(
        snapshot: SalesContentSnapshot,
        adminId: Int,
        message: String?,
    ): Inspection =
        this.copy(
            status = InspectionStatus.APPROVED,
            completedAt = LocalDateTime.now(),
            completedBy = Account(id = adminId, type = Account.Type.ADMIN),
            snapshot = snapshot,
            memo = message,
        )

    fun reject(
        snapshot: SalesContentSnapshot,
        adminId: Int,
        message: String,
    ): Inspection =
        this.copy(
            status = InspectionStatus.REJECTED,
            completedAt = LocalDateTime.now(),
            completedBy = Account(id = adminId, type = Account.Type.ADMIN),
            snapshot = snapshot,
            memo = message,
        )

    fun autoWithdraw(
        snapshot: SalesContentSnapshot,
        requester: Account,
    ): Inspection =
        this.copy(
            status = InspectionStatus.WITHDRAWN,
            completedAt = LocalDateTime.now(),
            completedBy = requester,
            snapshot = snapshot,
            memo = "재수정으로 인해 자동철회되었습니다."
        )

    fun withdraw(
        snapshot: SalesContentSnapshot,
        partnerId: Int,
        message: String?
    ): Inspection =
        this.copy(
            status = InspectionStatus.WITHDRAWN,
            completedAt = LocalDateTime.now(),
            completedBy = Account(id = partnerId, type = Account.Type.USER),
            snapshot = snapshot,
            memo = message ?: "사용자에 의해 철회되었습니다."
        )

    fun rollbackApproval(
        snapshot: SalesContentSnapshot = this.snapshot,
        message: String
    ): Inspection {
        if (this.status != InspectionStatus.APPROVED) {
            throw IllegalStateException("검수 상태가 APPROVED가 아닙니다.")
        }
        return this.copy(
            snapshot = snapshot,
            memo = memo.plus(" 승인 실패로 자동 보류처리되었습니다. $message"),
            status = InspectionStatus.REJECTED
        )
    }

    enum class InspectionStatus(val code: Int) {
        IMMEDIATELY(0),
        WAITING(1),
        APPROVED(2),
        REJECTED(3),
        WITHDRAWN(4),
    }

    companion object {
        fun requestByPartner(
            salesContentSnapshot: SalesContentSnapshot,
            salesContentDiff: SalesContentDiff,
            partnerId: Int,
        ): Inspection {
            val now = LocalDateTime.now()
            val account = Account(
                type = Account.Type.USER,
                id = partnerId,
            )
            return if (salesContentDiff.hasCriticalFields) {
                Inspection(
                    id = null,
                    productId = salesContentSnapshot.product.id,
                    status = InspectionStatus.WAITING,
                    snapshot = salesContentSnapshot,
                    modified = salesContentDiff,
                    createdBy = account,
                    createdAt = now,
                    completedBy = null,
                    completedAt = null,
                    memo = null,
                )
            } else {
                Inspection(
                    id = null,
                    productId = salesContentSnapshot.product.id,
                    status = InspectionStatus.IMMEDIATELY,
                    snapshot = salesContentSnapshot,
                    modified = salesContentDiff,
                    createdBy = account,
                    createdAt = now,
                    completedBy = account,
                    completedAt = now,
                    memo = null,
                )
            }
        }

        fun requestByAdmin(
            salesContentSnapshot: SalesContentSnapshot,
            salesContentDiff: SalesContentDiff,
            adminId: Int,
        ): Inspection {
            val now = LocalDateTime.now()
            val account = Account(
                type = Account.Type.ADMIN,
                id = adminId,
            )
            return Inspection(
                id = null,
                productId = salesContentSnapshot.product.id,
                status = InspectionStatus.IMMEDIATELY,
                snapshot = salesContentSnapshot,
                modified = salesContentDiff,
                createdBy = account,
                createdAt = now,
                completedBy = account,
                completedAt = now,
                memo = null,
            )
        }
    }
}