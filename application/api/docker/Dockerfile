FROM adoptopenjdk:11.0.11_9-jdk-hotspot as app

ARG APP_PROFILE=local
ENV GRPC_HEALTH_PROBE_VERSION=v0.4.5 \
    APP_PROFILE=${APP_PROFILE} \
    TZ=Asia/Seoul

WORKDIR /app

#--- setting up the Health probe for liveness setup
RUN curl -Lsfo /bin/grpc_health_probe https://github.com/grpc-ecosystem/grpc-health-probe/releases/download/${GRPC_HEALTH_PROBE_VERSION}/grpc_health_probe-linux-amd64 && \
    chmod a+x /bin/grpc_health_probe && \
    #--- adding datadog java agent for apm
    curl -Lso dd-java-agent.jar https://dtdg.co/latest-java-tracer

# copy library
WORKDIR /app
COPY build/libs/* /app/

# entry point
ENTRYPOINT java \
  -XX:+UseContainerSupport \
  -XX:InitialRAMPercentage=75 \
  -XX:MaxRAMPercentage=75 \
  -XshowSettings:vm \
  -Dfile.encoding=UTF-8 \
  -Djava.net.preferIPv4Stack=true \
  -jar /app/api.jar \
  --spring.profiles.active=${APP_PROFILE}
