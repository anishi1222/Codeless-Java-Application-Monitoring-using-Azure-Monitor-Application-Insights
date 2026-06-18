# Helidon Quickstart SE

Sample Helidon SE project that includes multiple REST operations. This application is used to demonstrate codeless Java monitoring with [Azure Monitor Application Insights](https://learn.microsoft.com/azure/azure-monitor/app/java-standalone-telemetry-processors).

## Prerequisites

- JDK 25+
- Maven 3.x
- The `MP_URL` environment variable must be set to the base URL of the running Helidon MP service (e.g., `http://localhost:8081`) before starting this application. It is required by the `/greet/greet2mp/{name}` endpoint, which proxies requests to the MP service. If `MP_URL` is not set, that endpoint will throw an exception at runtime.

## Build and run

```bash
export MP_URL=http://localhost:8081
mvn package
java -jar target/helidon-quickstart-se.jar
```

To enable monitoring with the Application Insights Java agent, download the agent jar and set the connection string:

```bash
export MP_URL=http://localhost:8081
export APPLICATIONINSIGHTS_CONNECTION_STRING=<your-connection-string>
java -javaagent:/path/to/applicationinsights-agent.jar -jar target/helidon-quickstart-se.jar
```

## Exercise the application

```
curl -X GET http://localhost:8080/greet
{"message":"Hello World!"}

curl -X GET http://localhost:8080/greet/Joe
{"message":"Hello Joe!"}

curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Hola"}' http://localhost:8080/greet/greeting

curl -X GET http://localhost:8080/greet/Jose
{"message":"Hola Jose!"}
```

The `/greet/greet2mp/{name}` endpoint proxies the request to the Helidon MP service (requires `MP_URL` to be set):

```
curl -X GET http://localhost:8080/greet/greet2mp/Joe
{"message":"Hello Joe!"}
```

## Try health and metrics

```
curl -s -X GET http://localhost:8080/observe/health
{"outcome":"UP",...
. . .

# Prometheus Format
curl -s -X GET http://localhost:8080/observe/metrics
# TYPE base:gc_g1_young_generation_count gauge
. . .

# JSON Format
curl -H 'Accept: application/json' -X GET http://localhost:8080/observe/metrics
{"base":...
. . .

```

## Build the Docker Image

The Docker image bundles the Application Insights Java agent. Set `APPLICATIONINSIGHTS_CONNECTION_STRING` and `MP_URL` when running the container.

```
docker build -t helidon-quickstart-se .
```

## Start the application with Docker

```
docker run --rm -p 8080:8080 \
  -e "APPLICATIONINSIGHTS_CONNECTION_STRING=<your-connection-string>" \
  -e "MP_URL=http://<mp-host>:8081" \
  helidon-quickstart-se:latest
```

Exercise the application as described above.

## Deploy the application to Kubernetes

```
kubectl cluster-info                        # Verify which cluster
kubectl get pods                            # Verify connectivity to cluster
kubectl create -f app.yaml                  # Deploy application
kubectl get pods                            # Wait for quickstart pod to be RUNNING
kubectl get service helidon-quickstart-se   # Get service info
```

Note the PORTs. You can now exercise the application as you did before but use the second
port number (the NodePort) instead of 8080.

After you're done, cleanup.

```
kubectl delete -f app.yaml
```
