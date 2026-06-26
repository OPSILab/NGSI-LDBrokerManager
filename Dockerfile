# Build arguments for version overrides
# Override via: docker build --build-arg JDK_VERSION=17 --build-arg JRE_VERSION=17
ARG JDK_VERSION=17
ARG JRE_VERSION=17

FROM eclipse-temurin:${JDK_VERSION}-jdk AS build
WORKDIR /workspace/app

COPY mvnw .
RUN chmod +x mvnw
RUN chmod 777 mvnw
# clean up the file
RUN sed -i 's/\r$//' mvnw
COPY .mvn .mvn
COPY pom.xml .

COPY src src


RUN ./mvnw -f /workspace/app/pom.xml install
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:${JRE_VERSION}-jre
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","it.eng.ngsild.broker.manager.BrokerManagerApp"]
