package dmk.poc.websocket.config;

import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
public class UserPrincipalHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String username = null;

        try {
            username = org.springframework.web.util.UriComponentsBuilder.fromUri(request.getURI())
                    .build()
                    .getQueryParams()
                    .getFirst("username");
            if (username != null && !username.trim().isEmpty()) {
                log.info("Interceptor username={}", username);
            } else {
                username = null; // Invalid username
            }
        } catch (Exception e) {
            log.error("Error parsing username from query parameters", e);
        }

        return username != null ? new UserPrincipal(username) : null;
    }
}
