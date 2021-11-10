# 1st stage, build the app
FROM maven:3.8.3-openjdk-17-slim as build
WORKDIR /helidon

# Create a first layer to cache the "Maven World" in the local repository.
# Incremental docker builds will always resume after that, unless you update
# the pom
ADD pom.xml .
RUN mvn package -Dmaven.test.skip -Declipselink.weave.skip

# Do the Maven build!
# Incremental docker builds will resume here when you change sources
ADD src src
RUN mvn package -DskipTests
RUN echo "done!"

# 2nd stage, build the runtime image
FROM openjdk:17.0.1-jdk-slim
ARG version="3.2.2"
WORKDIR /helidon

# Copy the binary built in the 1st stage
COPY --from=build /helidon/target/helidon-quickstart-mp.jar ./
COPY --from=build /helidon/target/libs ./libs
ADD https://github.com/microsoft/ApplicationInsights-Java/releases/download/${version}/applicationinsights-agent-${version}.jar ./applicationinsights-agent.jar

#CMD ["java", "-jar", "helidon-quickstart-mp.jar"]
CMD ["java", "-javaagent:applicationinsights-agent.jar", "-jar", "helidon-quickstart-mp.jar"]

EXPOSE 8080