# Codeless Java Application Monitoring using Azure Monitor Application Insights

This repository contains the sample applications used in these articles about monitoring Java applications with Azure Monitor Application Insights.

- English edition: https://medium.com/@Logico_jp/codeless-java-application-monitoring-using-azure-monitor-application-insights-5f05b339dd87
- Japanese edition: https://logico-jp.io/2020/04/16/monitor-java-applications-via-java-agent-newly-introduced-to-azure-monitor-application-insights/

## Repository contents

- [helidon-quickstart-mp](helidon-quickstart-mp) - Helidon MP sample application
- [helidon-quickstart-se](helidon-quickstart-se) - Helidon SE sample application that also calls the MP sample

Each sample directory includes its own README with build, run, Docker, and Kubernetes instructions:

- [helidon-quickstart-mp/README.md](helidon-quickstart-mp/README.md)
- [helidon-quickstart-se/README.md](helidon-quickstart-se/README.md)

## Prerequisites

- JDK 25 or later for local Maven builds
- Docker, if you want to build or run the container images
- Kubernetes, if you want to deploy the samples with the provided manifests

## Important notes

- Both sample applications listen on port `8080` by default. If you run them on the same host, change the port for one of them.
- Set the `MP_URL` environment variable before starting the Helidon SE sample. The SE application uses it when calling the Helidon MP sample from the `/greet2mp/{name}` endpoint.
- Each sample Dockerfile defines an Application Insights agent build argument named `version`. The default value is `3.7.8`, and you can override it with `--build-arg version=<agent-version>` when building the image.