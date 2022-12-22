package se.ohou.commerce.catalog.api.core.salesapplication.controller.external

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import se.ohou.commerce.catalog.api.common.support.audit.Account
import se.ohou.commerce.catalog.api.common.support.audit.AuthenticationAccount
import se.ohou.commerce.catalog.api.core.salesapplication.usecase.AdminActionService
import se.ohou.commerce.catalog.api.core.salesapplication.usecase.AdminApproval

@RestController
@RequestMapping(AdminActionController.PATH)
class AdminActionController(
    private val adminActionService: AdminActionService,
) {

    @PostMapping("{salesApplicationId}/approve")
    fun approve(
        @PathVariable
        salesApplicationId: Long,
        @AuthenticationAccount(required = true, requiredAuthority = [Account.Type.ADMIN])
        requester: Account,
        @RequestBody
        content: AdminApprovalDto.Request
    ): AdminApprovalDto.Response {
        val command = AdminApproval.Command(
            salesApplicationId = salesApplicationId,
            message = content.message,
            adminId = requester.id
        )
        val result = adminActionService.approve(command)
        return AdminApprovalDto.Response(
            goodsId = requireNotNull(result.salesApplication.goodsId)
        )
    }

    companion object {
        const val PATH = "/sales-application"
    }
}