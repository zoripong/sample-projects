package se.ohou.commerce.catalog.api.core.salesapplication.infra.messaging

import se.ohou.commerce.catalog.api.core.salesapplication.usecase.AdminActionService
import se.ohou.commerce.catalog.api.core.salesapplication.usecase.PartnerActionService

interface SalesApplicationProducer :
    AdminActionService.SalesApplicationProducer,
    PartnerActionService.SalesApplicationProducer
