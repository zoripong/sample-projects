package se.ohou.commerce.api.config.armeria

import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.server.ServerBuilder
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.docs.DocServiceFilter
import com.linecorp.armeria.server.healthcheck.HealthChecker
import com.linecorp.armeria.server.tomcat.TomcatService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import io.grpc.BindableService
import io.grpc.health.v1.HealthCheckResponse
import io.grpc.protobuf.services.HealthStatusManager
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.reflection.v1alpha.ServerReflectionGrpc
import org.apache.catalina.connector.Connector
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import se.ohou.clay.armeria.DatadogTraceService
import com.linecorp.armeria.server.grpc.GrpcService as GRPCService

@Configuration
class ArmeriaConfig(
    @Value("\${grpc.docs.enabled}")
    val docsEnabled: Boolean,
) {

    private fun getConnector(applicationContext: ServletWebServerApplicationContext): Connector {
        val container = applicationContext.webServer as TomcatWebServer
        container.start()
        return container.tomcat.connector
    }

    @Bean
    fun healthCheckManager() =
        HealthStatusManager()
            .apply {
                setStatus(
                    HealthStatusManager.SERVICE_NAME_ALL_SERVICES,
                    HealthCheckResponse.ServingStatus.SERVING,
                )
            }

    @Bean
    fun tomcatConnectorHealthChecker(applicationContext: ServletWebServerApplicationContext): HealthChecker {
        val connector: Connector = getConnector(applicationContext)
        return HealthChecker { connector.state.isAvailable }
    }

    @Bean
    fun tomcatService(applicationContext: ServletWebServerApplicationContext): TomcatService {
        return TomcatService.of(getConnector(applicationContext))
    }

    @Bean
    fun autowiringGRPCService(
        services: List<BindableService>,
        healthCheckManager: HealthStatusManager,
    ): GRPCService {
        return GRPCService
            .builder()
            .apply { services.forEach(this::addService) }
            .addService(ProtoReflectionService.newInstance())
            .addService(healthCheckManager.healthService)
            .supportedSerializationFormats(GrpcSerializationFormats.values())
            .enableUnframedRequests(true)
            .useBlockingTaskExecutor(true)
            .enableHttpJsonTranscoding(true)
            .build()
    }

    @Bean
    fun armeriaServiceInitializer(tomcatService: TomcatService, gRPCService: GRPCService): ArmeriaServerConfigurator {
        return ArmeriaServerConfigurator { serverBuilder: ServerBuilder ->
            val builder = serverBuilder
                .service("prefix:/", tomcatService)
                .service(gRPCService, DatadogTraceService.newDecorator())
            if (docsEnabled) {
                builder.serviceUnder(
                    "/docs",
                    DocService
                        .builder()
                        .exclude(DocServiceFilter.ofServiceName(ServerReflectionGrpc.SERVICE_NAME))
                        .build()
                )
            }
        }
    }
}
