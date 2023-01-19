package com.daily.product.users.interceptor;

import com.daily.product.users.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final TokenProvider tokenProvider;

    public AuthInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("==> HandlerInterceptor preHandle.");
        String token = tokenProvider.resolveToken(request);
        boolean authorization = false;

        if (StringUtils.hasText(token)) {
            if (!tokenProvider.getEmail(token).isEmpty())
                authorization = true;
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("authorization", authorization);

        if (!authorization)
            new MappingJackson2HttpMessageConverter().write(resultMap, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));

        return authorization;
    }
}
