# Codeless Java Application Monitoring using Azure Monitor Application Insights

This repository contains two Java sample applications used to demonstrate **codeless monitoring** with the Azure Monitor Application Insights Java agent.

The samples are based on:
- `codeless-micronaut` (Micronaut)
- `codeless-spring` (Spring Boot)

## Purpose

Use these apps to validate how Application Insights captures telemetry (requests, dependencies, failures, and performance) **without changing application code for instrumentation**.

- The Java agent is attached at runtime using `-javaagent:applicationinsights-agent.jar`.
- Dockerfiles download the agent from the official `microsoft/ApplicationInsights-Java` releases.

## Repository structure

- `codeless-micronaut`: Micronaut app exposing `/greet` endpoints.
- `codeless-spring`: Spring Boot app exposing `/greet` endpoints and an additional `/greet/greet2mp/{name}` endpoint that calls the Micronaut app via `MP_URL`.

Each sample directory includes its own README with build and run instructions.

## Prerequisites

- Docker (recommended for easiest setup)
- Azure Application Insights connection string
- For local Maven runs: JDK 25+

## Notes

- These sample applications listen on `8080` by default. If you use them on the same node, modify the listening port for one sample application.
- Set the `MP_URL` environment variable before invoking the Spring Boot sample application's `/greet/greet2mp/{name}` endpoint.
  - If the environment variable is not specified, the Spring Boot sample application returns an MP service error because this environment variable is used to call an API hosted on the Micronaut sample application.
  - This environment variable is used by `MpGreetingClient.java`.
- ARG `version` in each Dockerfile stands for the Application Insights agent version. The default is `3.7.8`; specify it manually in the Dockerfile or with `--build-arg` when creating images.

## Related article

- English Edition: https://medium.com/@Logico_jp/codeless-java-application-monitoring-using-azure-monitor-application-insights-5f05b339dd87
- Japanese edition: https://logico-jp.io/2020/04/16/monitor-java-applications-via-java-agent-newly-introduced-to-azure-monitor-application-insights/
