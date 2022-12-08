@Repository
class InspectionRepositoryImpl(
    private val inspectionRepository: InspectionMongoRepository,
) : InspectionRepository {
    override fun updateInspection(inspection: Inspection): Inspection
}