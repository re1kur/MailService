package re1kur.mailService.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MailConfiguration {

    @Value("${custom.datasource.baseUrl}")
    private String datasourceBaseUrl;

    @Value("${custom.queue.verification}")
    private String verificationQueue;

    @Value("${custom.queue.notification}")
    private String notificationQueue;

    @Bean
    public Queue verificationQueue() {
        return new Queue(verificationQueue);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(notificationQueue);
    }

    @Bean
    public Queue testQueue() {
        return new Queue("test");
    }

    @Bean
    public WebClient datasourceClient() {
        return WebClient.builder()
                .baseUrl(datasourceBaseUrl)
                .build();
    }

}
