package se.ohou.commerce.catalog.api.common.config.audit

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.server.ResponseStatusException
import se.ohou.commerce.catalog.api.common.support.audit.Account
import se.ohou.commerce.catalog.api.common.support.audit.AuthenticationAccount

@Component
class AuthenticationAccountResolver : HandlerMethodArgumentResolver {
    // 어떤 파라미터를 지원하는지
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(AuthenticationAccount::class.java)
    }

    // 바인딩할 객체 생성
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Account? {
        val authenticationAccount = parameter.parameterAnnotations
            .find { it.annotationClass == AuthenticationAccount::class } as AuthenticationAccount

        val accountHeader = webRequest.getHeader(OHOUSE_CATALOG_ACCOUNT)

        if (accountHeader.isNullOrEmpty()) {
            if (authenticationAccount.required) {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
            }
            return null
        }

        val account = Account(accountHeader)

        if (authenticationAccount.requiredAuthority.isNotEmpty()) {
            if (account.type !in authenticationAccount.requiredAuthority) {
                throw ResponseStatusException(HttpStatus.FORBIDDEN)
            }
        }

        return account
    }

    companion object {
        const val OHOUSE_CATALOG_ACCOUNT = "Ohouse-Catalog-Account"
    }
}
