# Codeless Spring Boot Application

Sample Spring Boot 4 web application instrumented with the Azure Monitor Application Insights Java agent at runtime.

## Build and run

With JDK 25+
```bash
mvn package
java -jar target/codelessapp.jar
```

## Exercise the application

```bash
curl -X GET http://localhost:8080/greet
{"message":"Hello World!"}

curl -X GET http://localhost:8080/greet/Joe
{"message":"Hello Joe!"}

curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Hola"}' http://localhost:8080/greet/greeting

curl -X GET http://localhost:8080/greet/Jose
{"message":"Hola Jose!"}
```

If the Micronaut MP sample application is running, set `MP_URL` to call it from this application.

```bash
MP_URL=http://localhost:8081 java -jar target/codelessapp.jar
curl -X GET http://localhost:8080/greet/greet2mp/Joe
```

## Try health and metrics

```bash
curl -s -X GET http://localhost:8080/actuator/health
{"status":"UP",...}

curl -s -X GET http://localhost:8080/actuator/metrics
{"names":[...]}

curl -s -X GET http://localhost:8080/actuator/prometheus
# HELP ...
```

## Build the Docker Image

```bash
docker build -t codeless-spring .
```

## Start the application with Docker

```bash
docker run --rm -p 8080:8080 \
  -e "APPLICATIONINSIGHTS_CONNECTION_STRING=InstrumentationKey=..." \
  -e "MP_URL=http://micronaut-service:8080" \
  codeless-spring:latest
```

Exercise the application as described above

## Deploy the application to Kubernetes

```bash
kubectl cluster-info                        # Verify which cluster
kubectl get pods                            # Verify connectivity to cluster
kubectl create -f app.yaml                  # Deploy application
kubectl get pods                            # Wait for quickstart pod to be RUNNING
kubectl get service codeless-spring         # Get service info
```

Note the PORTs. You can now exercise the application as you did before but use the second
port number (the NodePort) instead of 8080.

After you’re done, cleanup.

```bash
kubectl delete -f app.yaml
```
