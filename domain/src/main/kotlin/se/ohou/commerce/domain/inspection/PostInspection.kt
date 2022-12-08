/**
 * 판매중 상품 수정 사후 검수 내역
 *
 * @property id 식별자
 * @property productId 수정하는 상품 Id
 * @property status 사후 검수 상태
 * @property postInspection 사후 검수 항목 변경 내역
 * @property createdBy 사후 검수 상품 판매자
 * @property createdAt 사후 검수 생성 시각
 * @property completedBy 사후 검수한 사용자
 * @property completedAt 사후 검수 완료 시각
 * @property modifiedPostInspectionFields 수정된 사후 검수 항목 리스트
 */
data class PostInspection(
    val id: String?,
    val inspectionId: String,
    val productId: Int,
    val status: PostInspectionStatus,
    val snapshot: SalesContentSnapshot,
    val postInspection: List<DiffNode<*>>,
    val createdBy: Account,
    val createdAt: LocalDateTime,
    val completedBy: Account?,
    val completedAt: LocalDateTime?
) {
    val modifiedPostInspectionFields: List<InspectionField>
        get() {
            if (postInspection.isEmpty()) return emptyList()
            return InspectionField.values().toList()
                .filter { inspectionField ->
                    inspectionField.step == InspectionTarget.Step.POST &&
                            postInspection.any { inspectionField.isTarget(it) }
                }
        }

    fun changeStatus(
        status: PostInspectionStatus,
        completedBy: Account,
        completedAt: LocalDateTime = LocalDateTime.now()
    ) = this.copy(
        status = status,
        completedBy = completedBy,
        completedAt = completedAt
    )

    enum class PostInspectionStatus(
        val description: String,
    ) {
        INCOMPLETE("검수필요") {
            override val canModifyTo: Set<PostInspectionStatus> by lazy {
                setOf(COMPLETE, REQUESTED_CHANGE)
            }
        },
        COMPLETE("검수완료"),
        REQUESTED_CHANGE("수정필요") {
            override val canModifyTo: Set<PostInspectionStatus> by lazy {
                setOf(CHANGED)
            }
        },
        CHANGED("수정완료");

        protected open val canModifyTo: Set<PostInspectionStatus> by lazy {
            setOf()
        }

        fun isAvailableStatus(status: PostInspectionStatus): Boolean = status in canModifyTo
    }

    companion object {
        fun createByInspection(inspection: Inspection) =
            with(inspection) {
                PostInspection(
                    id = null,
                    inspectionId = requireNotNull(id),
                    productId = productId,
                    snapshot = inspection.snapshot,
                    status = PostInspectionStatus.INCOMPLETE,
                    postInspection = postInspection,
                    createdBy = requireNotNull(completedBy),
                    createdAt = requireNotNull(completedAt),
                    completedBy = null,
                    completedAt = null
                )
            }
    }
}
