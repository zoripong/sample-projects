package se.ohou.commerce.api.sample.adapter.input

import org.springframework.stereotype.Component
import se.ohou.api.sampleecho.EchoRequest
import se.ohou.api.sampleecho.EchoServiceGrpcKt
import se.ohou.api.sampleecho.echoResponse

@Component
class EchoGrpcImpl : EchoServiceGrpcKt.EchoServiceCoroutineImplBase() {
    override suspend fun echo(request: EchoRequest) =
        echoResponse {
            echoedMessage = request.message
        }
}
