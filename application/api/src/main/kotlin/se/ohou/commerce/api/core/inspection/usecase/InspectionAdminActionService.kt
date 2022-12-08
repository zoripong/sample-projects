@Service
class InspectionAdminActionService(
    private val salesContentDiffBuilder: SalesContentDiffBuilder,
    private val autoWithdrawUnit: AutoWithdrawUnit,
    private val productSnapshotUnit: ProductSnapshotUnit,
    private val postInspectionUnit: PostInspectionUnit,
    private val legacyProductDtoReadUnit: LegacyProductDtoReadUnit,
    private val sellingProductValidationUnit: SellingProductValidationUnit,
    private val updateGoodsPortWithAB: AbComponentSelector<UpdateGoodsPort>
) : Loggable {

    @Transactional(readOnly = false, transactionManager = ChainedTransactionManagerConfiguration.CHAINED_TRX_MANAGER)
    override fun approve(
        command: InspectionAdminActionUseCases.ApproveCommand,
    ): InspectionAdminActionUseCases.AdminActionResult {
        val inspection = readInspectionPort.findById(command.inspectionId)

        // 판매가 또는 수수료가 변경되는 옵션 id만 할인중인지 검수
        discountCheckPort.checkForOptionsWithDiscount(
            inspection.modified.changedCostOptionIds
        )

        val isAlreadyFinished = inspection.isFinished

        if (isAlreadyFinished) {
            return InspectionAdminActionUseCases.AdminActionResult(true, inspection)
        }

        val approvedInspection = inspection
            .approve(
                snapshot = productSnapshotUnit.createSnapshot(inspection.productId),
                adminId = command.adminId,
                message = command.message,
            )
            .let {
                writeInspectionPort.save(it)
            }

        if (approvedInspection.status == Inspection.InspectionStatus.APPROVED) {
            // 쓰기 구간 이관 (ohs_admin -> catalog-mgmt) TC
            if (command.saveImmediately) {
                updateProductionPort.updateByAdmin(
                    before = approvedInspection.snapshot.product,
                    after = approvedInspection.modified.product,
                    adminId = command.adminId,
                )
            }
            // 신규 catalog Dual Write TC
            runBlocking {
                updateGoodsPortWithAB
                    .getComponent(
                        experimentId = UPDATE_GOODS_BY_APPROVAL_DUAL_WRITE_TC_ID,
                        visitorId = command.adminId.toString(),
                    )
                    .update(
                        product = approvedInspection.modified.product,
                    )
            }
        }

        if (approvedInspection.readyToPostInspection) {
            postInspectionUnit.createPostInspection(approvedInspection)
        }

        return InspectionAdminActionUseCases.AdminActionResult(false, approvedInspection)
    }

    interface InspectionRepository {
        fun updateInspection(inspection: Inspection): Inspection
    }

    companion object {
        private const val UPDATE_GOODS_BY_ADMIN_DUAL_WRITE_TC_ID = 415
        private const val UPDATE_GOODS_BY_APPROVAL_DUAL_WRITE_TC_ID = 416
    }
}