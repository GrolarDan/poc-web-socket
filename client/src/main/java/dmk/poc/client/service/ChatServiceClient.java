package dmk.poc.client.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ChatServiceClient {
    private static final ParameterizedTypeReference<List<String>> STRING_LIST_TYPE = new ParameterizedTypeReference<>() {};
    private final RestClient restClient;

    public ChatServiceClient(RestClient.Builder restClientBuilder, @Value("${websocket.url}") String chatServiceUrl, @Value("${websocket.port}") String port) {
        this.restClient = restClientBuilder.baseUrl("http://%s:%s".formatted(chatServiceUrl, port)).build();
    }

    public List<String> getUsers() {
        return restClient.get()
                .uri("/chat/users")
                .retrieve()
                .body(STRING_LIST_TYPE);
    }
}
