package se.ohou.commerce.catalog.api.common.support.audit

data class Account(
    val type: Type,
    val id: Int,
) {
    constructor(accountString: String) : this(
        type = Type.valueOf(accountString.split(":")[0]),
        id = accountString.split(":")[1].toInt()
    )

    enum class Type {
        ADMIN, USER, SELLER
    }

    fun isAdmin() = (type == Type.ADMIN)

    fun isUser() = (type == Type.USER)
}
