# Codeless Java Application Monitoring using Azure Monitor Application Insights

This repository contains two Helidon 4.4.1 sample applications used to demonstrate **codeless Java monitoring** with the Azure Monitor Application Insights Java agent.

The samples are based on:
- `helidon-quickstart-mp` (Helidon MP)
- `helidon-quickstart-se` (Helidon SE)

## Purpose

Use these apps to validate how Application Insights captures telemetry (requests, dependencies, failures, and performance) **without changing application code for instrumentation**.

- The Java agent is attached at runtime using `-javaagent:applicationinsights-agent.jar`.
- Dockerfiles download the agent from the official `microsoft/ApplicationInsights-Java` releases.

## Repository structure

- `helidon-quickstart-mp`  
  MicroProfile app exposing `/greet` endpoints.
- `helidon-quickstart-se`  
  Helidon SE app exposing `/greet` endpoints and an additional `/greet/greet2mp/{name}` endpoint that calls the MP app via `MP_URL`.

Both applications listen on `8080` by default.

## Prerequisites

- Docker (recommended for easiest setup)
- Azure Application Insights connection string
- For local Maven runs: JDK 25+

## Quick start with Docker

### 1) Build images

```bash
cd helidon-quickstart-mp
docker build -t helidon-quickstart-mp .

cd ../helidon-quickstart-se
docker build -t helidon-quickstart-se .
```

### 2) Run MP sample

```bash
docker run --rm -p 8080:8080 \
  -e "APPLICATIONINSIGHTS_CONNECTION_STRING=<your-connection-string>" \
  --name helidon-mp \
  helidon-quickstart-mp:latest
```

### 3) Run SE sample

> `helidon-quickstart-se` also needs `MP_URL` so `/greet/greet2mp/{name}` can call the MP service.

```bash
docker run --rm -p 8081:8080 \
  -e "APPLICATIONINSIGHTS_CONNECTION_STRING=<your-connection-string>" \
  -e "MP_URL=http://host.docker.internal:8080" \
  --name helidon-se \
  helidon-quickstart-se:latest
```

## API checks

MP sample:
```bash
curl http://localhost:8080/greet
curl http://localhost:8080/greet/Joe
curl -X PUT -H "Content-Type: application/json" \
  -d '{"greeting":"Hola"}' http://localhost:8080/greet/greeting
```

SE sample:
```bash
curl http://localhost:8081/greet
curl http://localhost:8081/greet/Joe
curl http://localhost:8081/greet/greet2mp/Joe
```

## Notes

- If both apps run on one host, use different external ports (for example `8080` and `8081`).
- `MP_URL` is required for SE-to-MP call flow (`/greet/greet2mp/{name}`).
- Dockerfiles support agent version override with build arg `version` (default `3.7.8`).

## Related article

- English: https://medium.com/@Logico_jp/codeless-java-application-monitoring-using-azure-monitor-application-insights-5f05b339dd87
- Japanese: https://logico-jp.io/2020/04/16/monitor-java-applications-via-java-agent-newly-introduced-to-azure-monitor-application-insights/
