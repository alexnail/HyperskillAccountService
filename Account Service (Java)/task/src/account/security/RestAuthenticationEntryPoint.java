package account.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public static final String ORIGINAL_REQUEST_URL = "jakarta.servlet.error.request_uri";
    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        /*log.debug(">>>> Attributes:");
        request.getAttributeNames().asIterator().forEachRemaining(name -> log.debug(">>>> {}: {}", name, request.getAttribute(name)));*/

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "User account is locked"); //User account is locked, "jakarta.servlet.error.message" returns "Unauthorized"
        body.put("path", request.getAttribute(ORIGINAL_REQUEST_URL).toString());

        response.getOutputStream().println(objectMapper.writeValueAsString(body));
    }
}
