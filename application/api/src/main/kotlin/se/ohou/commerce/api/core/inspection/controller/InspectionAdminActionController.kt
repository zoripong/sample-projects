package se.ohou.catalogmanagement.core.inspection.adapters.input

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import se.ohou.catalogmanagement.commons.support.aduit.Account
import se.ohou.catalogmanagement.commons.support.exception.http.BadRequestException
import se.ohou.catalogmanagement.commons.support.exception.http.NotFoundException
import se.ohou.catalogmanagement.commons.support.logging.AuditLogSender
import se.ohou.catalogmanagement.commons.support.logging.AuditLogSender.Companion.toJsonString
import se.ohou.catalogmanagement.commons.support.logging.Loggable
import se.ohou.catalogmanagement.commons.support.objectid.ValidObjectId
import se.ohou.catalogmanagement.commons.support.randomizer.TrafficOption
import se.ohou.catalogmanagement.core.inspection.adapters.input.mappers.InspectionApproveDtoMapper
import se.ohou.catalogmanagement.core.inspection.adapters.input.mappers.ProductUpdateMapper
import se.ohou.catalogmanagement.core.inspection.adapters.input.models.InspectionAdminActionDto
import se.ohou.catalogmanagement.core.inspection.adapters.input.models.ProductUpdateDto
import se.ohou.catalogmanagement.core.inspection.applications.port.input.InspectionAdminActionUseCases
import se.ohou.catalogmanagement.commons.support.aduit.AuthenticationAccount

@RestController
@RequestMapping(InspectionAdminActionController.PATH)
class InspectionAdminActionController(
    private val inspectionAdminActionService: InspectionAdminActionService,
    private val auditLogSender: AuditLogSender,
) : Loggable {

    @PostMapping("{inspectionId}/approve")
    fun approve(
        @PathVariable @ValidObjectId
        inspectionId: String,
        @RequestBody
        dto: InspectionAdminActionDto.Request,
        @AuthenticationAccount(required = true, requiredAuthority = [Account.Type.ADMIN])
        requester: Account,
        @RequestParam
        saveImmediatelyABOption: TrafficOption? = TrafficOption.A,
    ): InspectionAdminActionDto.Response {
        val saveImmediately = saveImmediatelyABOption == TrafficOption.B
        val command = InspectionAdminActionUseCases.ApproveCommand(
            inspectionId = inspectionId,
            message = dto.message,
            adminId = requester.id,
            saveImmediately = saveImmediately,
        )
        val result = try {
            inspectionAdminActionService.approve(command)
                .also {
                    auditLogSender.send(
                        action = ADMIN_APPROVE_INSPECTION_ACTION,
                        key = inspectionId,
                        userId = requester.id.toString(),
                        message = command.toJsonString(),
                    )
                }
        } catch (exception: NoSuchElementException) {
            throw NotFoundException(exception.message ?: "Cannot find data with id($inspectionId)", exception)
        }
        return InspectionApproveDtoMapper.toResponse(result)
    }

    companion object {
        const val PATH = "inspections/admin"
        private const val ADMIN_APPROVE_INSPECTION_ACTION = "admin|inspection-approve"
    }
}
