# Micronaut Quickstart

Sample Micronaut 5 web application that exposes the same greeting REST operations as the previous Helidon MP sample.

## Build and run

With JDK 25+
```bash
mvn package
java -jar target/codelessapp.jar
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

## Try health and metrics

```
curl -s -X GET http://localhost:8080/health
{"status":"UP",...}

# JSON metrics
curl -s -X GET http://localhost:8080/metrics
{"names":[...]}

# Prometheus format
curl -s -X GET http://localhost:8080/prometheus
# HELP process_uptime_seconds ...

```

## Build the Docker Image

```
docker build -t codeless-micronaut .
```

## Start the application with Docker

```
docker run --rm -p 8080:8080 codeless-micronaut:latest
```

Exercise the application as described above

## Deploy the application to Kubernetes

```
kubectl cluster-info                         # Verify which cluster
kubectl get pods                             # Verify connectivity to cluster
kubectl create -f app.yaml                   # Deploy application
kubectl get pods                             # Wait for quickstart pod to be RUNNING
kubectl get service codeless-micronaut       # Verify deployed service
```

Note the PORTs. You can now exercise the application as you did before but use the second
port number (the NodePort) instead of 8080.

After you’re done, cleanup.

```
kubectl delete -f app.yaml
```
