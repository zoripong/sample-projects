package se.ohou.commerce.catalog.api

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.extensions.spring.SpringExtension

@Suppress("unused")
class KotestConfig : AbstractProjectConfig() {
    /**
     * Kotest 에서 스프링의 Field injection 을 사용하기 위해 Extension을 등록합니다.
     * @href https://kotest.io/docs/extensions/spring.html
     */
    override fun extensions() = listOf(SpringExtension)
}
