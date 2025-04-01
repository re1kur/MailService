package re1kur.mailService.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class DefaultDatabaseClient implements DatabaseClient {
    private final WebClient client;

    @Autowired
    public DefaultDatabaseClient(WebClient client) {
        this.client = client;
    }

    @Override
    public List<String> getSubscribersEmails(String objectChangesName, Integer objectChangesId) {
        if (objectChangesName == null || objectChangesName.isEmpty()) {
            throw new IllegalArgumentException("objectChangesName cannot be null or empty");
        }
        Mono<List<String>> emailsMono = client.get()
                .uri(objectChangesName + "/readSubscribersEmails?id=" + objectChangesId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                });
        return emailsMono.block();
    }
}
