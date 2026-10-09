package shl_nyllet.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthenticationService {
    private final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    // private final String AUTH_TOKEN = ;
    @Value("${app.api-key}")
    private String AUTH_TOKEN;

    public Authentication getAuthentication(HttpServletRequest request) {
        String apikey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apikey == null || !apikey.equals(AUTH_TOKEN)) {
            throw new BadCredentialsException("Invalid API Key");
        }
        return new ApiKeyAuthentication(apikey, AuthorityUtils.NO_AUTHORITIES);
    }
}
