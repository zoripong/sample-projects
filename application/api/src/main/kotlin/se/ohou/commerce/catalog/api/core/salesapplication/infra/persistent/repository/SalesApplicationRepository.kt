package se.ohou.commerce.catalog.api.core.salesapplication.infra.persistent.repository

import se.ohou.commerce.catalog.api.core.salesapplication.usecase.AdminActionService
import se.ohou.commerce.catalog.api.core.salesapplication.usecase.PartnerActionService

interface SalesApplicationRepository :
    AdminActionService.SalesApplicationRepository,
    PartnerActionService.SalesApplicationRepository