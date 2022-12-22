package se.ohou.commerce.catalog.api.common.support.audit

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthenticationAccount(
    /**
     * 필수여부
     * true 인 경우 account 가 null 일 때 UNAUTHORIZED 을 throw 합니다
     * false 인 경우 account 를 nullable 로 처리합니다.
     */
    val required: Boolean = false,

    /**
     * 권한 상세 필수여부
     * empty 가 아닌 경우, requiredAuthority 의 모든 권한 중 한개이상 만족하지 않을 경우 throw forbidden 합니다.
     */
    val requiredAuthority: Array<Account.Type> = [],
)
