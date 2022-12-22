package se.ohou.commerce.catalog.api.common.support.messaging

interface Producer<K, V> {
    fun buildMessage(type: String, payload: V): Message<V>

    fun send(type: String, payload: V)

    fun send(
        type: String,
        payload: V,
        successCallback: () -> Unit,
        failedCallback: (exception: Exception) -> Unit,
    )

    fun send(type: String, key: K, payload: V)

    fun send(
        type: String,
        key: K,
        payload: V,
        successCallback: () -> Unit,
        failedCallback: (exception: Exception) -> Unit,
    )

    data class Message<T>(val type: String, val version: String, val body: T)
}
