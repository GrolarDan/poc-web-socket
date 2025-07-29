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
        String query = request.getURI().getQuery();
        if (query != null && query.startsWith("username=")) {
            username = query.substring("username=".length());
            log.info("Interceptor username={}", username);
        }

        return username != null ? new UserPrincipal(username) : null;
    }
}
