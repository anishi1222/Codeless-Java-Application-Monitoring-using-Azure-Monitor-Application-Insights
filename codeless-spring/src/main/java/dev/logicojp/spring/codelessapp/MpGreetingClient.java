package dev.logicojp.spring.codelessapp;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class MpGreetingClient {

    private final String mpUrl;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public MpGreetingClient(GreetingProperties properties,
                            ObjectMapper objectMapper,
                            RestClient.Builder restClientBuilder) {
        this.mpUrl = properties.getMpUrl();
        this.objectMapper = objectMapper;
        this.restClient = StringUtils.hasText(mpUrl) ? restClientBuilder.baseUrl(mpUrl).build() : null;
    }

    public GreetingMessage getGreeting(String name) {
        if (restClient == null) {
            throw new MpServiceException("MP_URL is not configured");
        }

        try {
            String responseBody = restClient.get()
                    .uri("/greet/{name}", name)
                    .retrieve()
                    .body(String.class);
            return readGreetingMessage(responseBody);
        } catch (JacksonException ex) {
            throw new InvalidMpResponseException("Invalid JSON response from MP service", ex);
        } catch (RestClientException ex) {
            throw new MpServiceException("MP service request failed", ex);
        }
    }

    private GreetingMessage readGreetingMessage(String responseBody) throws JacksonException {
        if (responseBody == null) {
            throw new InvalidMpResponseException("Empty MP service response");
        }
        GreetingMessage response = objectMapper.readValue(responseBody, GreetingMessage.class);
        if (response.message() == null) {
            throw new InvalidMpResponseException("Missing message in MP service response");
        }
        return response;
    }
}
