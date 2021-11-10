# Codeless Java Application Monitoring using Azure Monitor Application Insights

This is a repository for sample applications used in the following article.

- English Edition : https://medium.com/@Logico_jp/codeless-java-application-monitoring-using-azure-monitor-application-insights-5f05b339dd87
- Japanese edition : https://logico-jp.io/2020/04/16/monitor-java-applications-via-java-agent-newly-introduced-to-azure-monitor-application-insights/

## NOTICE

- These sample applications listen to 8080/tcp. If you use them on the same node, you have to modify listening port for one sample application.
- The environment variable "MP_URL" should be set before invoking Helidon SE sample application.
  - If the environment variable is not specified, Helidon SE sample application throws exception since this environment variable is used to call an API hosted on Helidon MP sample application.
  - This environment variable is used in specific to GreetingService.java.
  - This execption is described in "unhandled exception" in the passage listed above.
- ARG "version" in each Dockerfile stands for Application Insights agent version. You should specify it manually in Dockerfile or specify with --built-arg when invoking "docker build".