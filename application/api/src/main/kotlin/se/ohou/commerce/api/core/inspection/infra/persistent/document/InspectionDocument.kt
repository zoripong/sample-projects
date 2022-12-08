
@QueryEntity
@Document("Inspections")
data class InspectionDocument(

    @MongoId(value = FieldType.OBJECT_ID)
    val id: String? = null,

    val productId: Int,
    val status: InspectionStatus = InspectionStatus.WAITING,

    @Embedded
    @QueryEmbedded
    val modified: InspectionProduct,

    @Embedded
    @QueryEmbedded
    val snapshot: InspectionProduct,

    @Embedded
    @QueryEmbedded
    val preInspection: List<Diff>?,

    @Embedded
    @QueryEmbedded
    val postInspection: List<Diff>?,

    @CreatedBy
    @Column(nullable = false, updatable = false)
    val createdBy: Account? = null,

    @CreatedDate
    @Column(nullable = false)
    val createdAt: LocalDateTime,

    val memo: String? = null,
    val completedBy: Account? = null,
    val completedAt: LocalDateTime? = null,
) {
    enum class InspectionStatus(
        val mappedDomainStatus: Inspection.InspectionStatus
    ) {
        IMDTL(Inspection.InspectionStatus.IMMEDIATELY),
        WAITING(Inspection.InspectionStatus.WAITING),
        APPROVE(Inspection.InspectionStatus.APPROVED),
        REJECT(Inspection.InspectionStatus.REJECTED),
        WTHDR(Inspection.InspectionStatus.WITHDRAWN);

        companion object {
            private val domainMap = values().associateBy(InspectionStatus::mappedDomainStatus)

            fun get(status: Inspection.InspectionStatus): InspectionStatus =
                domainMap[status]
                    ?: throw IllegalStateException("cannot find mapped InspectionStatus for $status")
        }
    }
}

fun Inpsection.toDocument(): InspectionDocument = TODO()
