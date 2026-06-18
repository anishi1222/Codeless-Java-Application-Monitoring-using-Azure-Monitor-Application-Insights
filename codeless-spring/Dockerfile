# 1st stage, build the app
FROM maven:3.9.16-eclipse-temurin-25 AS build

WORKDIR /app

# Create a first layer to cache the "Maven World" in the local repository.
# Incremental docker builds will always resume after that, unless you update
# the pom
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -DskipTests dependency:go-offline

# Do the Maven build!
# Incremental docker builds will resume here when you change sources
COPY src src
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests

# 2nd stage, build the runtime image
FROM mcr.microsoft.com/openjdk/jdk:25-distroless
ARG version="3.7.8"
EXPOSE 8080
WORKDIR /app

# Copy the binary built in the 1st stage
COPY --from=build /app/target/codelessapp.jar ./app.jar
ADD https://github.com/microsoft/ApplicationInsights-Java/releases/download/${version}/applicationinsights-agent-${version}.jar ./applicationinsights-agent.jar

CMD ["-javaagent:applicationinsights-agent.jar", "-jar", "app.jar"]
