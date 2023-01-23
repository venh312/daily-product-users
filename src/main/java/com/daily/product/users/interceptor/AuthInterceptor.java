package com.daily.product.users.interceptor;

import com.daily.product.users.jwt.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final TokenProvider tokenProvider;

    public AuthInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        log.info("==> HandlerInterceptor preHandle.");
        HashMap<String, Object> resultMap = new HashMap<>();
        String token = tokenProvider.resolveToken(request);
        boolean authorization = false;

        if (StringUtils.hasText(token)) {
            try {
                if (tokenProvider.getId(token) != null)
                    authorization = true;
            } catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
                e.printStackTrace();
                resultMap.put("msg", "Invalid token.");
            }
        }

        resultMap.put("authorization", authorization);

        if (!authorization)
            new MappingJackson2HttpMessageConverter().write(resultMap, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));

        return authorization;
    }
}
