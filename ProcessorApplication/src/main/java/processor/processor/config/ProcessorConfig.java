package processor.processor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "processor")
public class ProcessorConfig {
    private String ingressBaseUrl;
    private int workerCount = 4; // default

    public String getIngressBaseUrl() {
        return ingressBaseUrl;
    }

    public void setIngressBaseUrl(String ingressBaseUrl) {
        this.ingressBaseUrl = ingressBaseUrl;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }
}