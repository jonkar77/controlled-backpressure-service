package processor.processor.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import processor.processor.model.Task;

import java.util.Optional;

@Component
public class IngressClient {
    private static final Logger log = LoggerFactory.getLogger(IngressClient.class);

    private final RestTemplate restTemplate;
    private final String ingressBaseUrl;

    public IngressClient(RestTemplate restTemplate, String ingressBaseUrl) {
        this.restTemplate = restTemplate;
        this.ingressBaseUrl = ingressBaseUrl;
    }

    public Optional<Task> claimTask() {
        try {
            String url = ingressBaseUrl + "/tasks/claim";
            ResponseEntity<Task> response = restTemplate.getForEntity(url, Task.class);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                return Optional.empty();
            }

            if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
                return Optional.of(response.getBody());
            }

            log.warn("Unexpected response from claim endpoint: {}", response.getStatusCode());
            return Optional.empty();

        } catch (Exception e) {
            log.error("Error claiming task from Ingress", e);
            return Optional.empty();
        }
    }
}