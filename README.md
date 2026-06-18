# Codeless Java Application Monitoring using Azure Monitor Application Insights

This is a repository for sample applications used in the following article.

- English Edition : https://medium.com/@Logico_jp/codeless-java-application-monitoring-using-azure-monitor-application-insights-5f05b339dd87
- Japanese edition : https://logico-jp.io/2020/04/16/monitor-java-applications-via-java-agent-newly-introduced-to-azure-monitor-application-insights/

## NOTICE

- These sample applications listen to 8080/tcp. If you use them on the same node, you have to modify listening port for one sample application.
- The environment variable "MP_URL" should be set before invoking the Spring Boot sample application's `/greet/greet2mp/{name}` endpoint.
  - If the environment variable is not specified, the Spring Boot sample application returns an MP service error because this environment variable is used to call an API hosted on the MP sample application.
  - This environment variable is used by `MpGreetingClient.java`.
- ARG "version" in each Dockerfile stands for Application Insights agent version. The default is 3.7.8; specify it manually in Dockerfile or with --build-arg when creating images.