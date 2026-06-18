# Codeless Java Application Monitoring using Azure Monitor Application Insights

This repository contains sample Helidon applications for demonstrating codeless Java monitoring with Azure Monitor Application Insights.

## Related articles

- English edition: https://medium.com/@Logico_jp/codeless-java-application-monitoring-using-azure-monitor-application-insights-5f05b339dd87
- Japanese edition: https://logico-jp.io/2020/04/16/monitor-java-applications-via-java-agent-newly-introduced-to-azure-monitor-application-insights/

## Repository contents

- `/helidon-quickstart-mp` - Helidon MP sample application
- `/helidon-quickstart-se` - Helidon SE sample application

Each sample includes its own `README.md` with build, run, Docker, and Kubernetes instructions.

## Important notes

- Both sample applications listen on port `8080`. If you run them on the same host at the same time, change the port for one of them.
- Set the `MP_URL` environment variable before starting the Helidon SE sample.
  - The SE sample uses this value when calling the Helidon MP sample from `GreetService.java`.
  - If `MP_URL` is missing, the SE sample can fail when the cross-service call is invoked.
- Each Dockerfile uses the build argument `version` for the Application Insights Java agent version.
  - The default value is `3.7.8`.
  - You can change it directly in the Dockerfile or override it with `docker build --build-arg version=<agent-version>`.