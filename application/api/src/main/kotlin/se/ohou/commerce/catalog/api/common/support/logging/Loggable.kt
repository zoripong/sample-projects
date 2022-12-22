package se.ohou.commerce.catalog.api.common.support.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface Loggable {
    val log: Logger get() = LoggerFactory.getLogger(javaClass)
}
