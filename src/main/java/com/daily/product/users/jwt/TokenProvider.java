package com.daily.product.users.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
JWT(Json Web Token)란 Json 포맷을 이용하여 사용자에 대한 속성을 저장하는 Claim 기반의 Web Token이다.
JWT는 토큰 자체를 정보로 사용하는 Self-Contained 방식으로 정보를 안전하게 전달한다. */
@Slf4j
@Component
public class TokenProvider {
    // 30분
    private final long ACCESS_TOKEN_SECOND = 60 * 30;
    // 1개월
    private final long REFRESH_TOKEN_SECOND = 60 * 60 * 24 * 7;
    private final String secretKey;

    public TokenProvider(@Value("${global.jwt.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    private Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String email) {
        return doGenerateToken(email, ACCESS_TOKEN_SECOND * 1000l);
    }

    public HashMap<String, String> generateRefreshToken(String email) {
        HashMap<String, String> hMap = new HashMap<>();
        hMap.put("token", doGenerateToken(email, REFRESH_TOKEN_SECOND * 1000l));
        hMap.put("expiration", String.valueOf(REFRESH_TOKEN_SECOND));
        return hMap;
    }

    public String getEmail(String token) {
        return validTokenAndReturnBody(token).get("email", String.class);
    }
	
	// 토큰 생성
    private String doGenerateToken(String email, Long expireTime) {
        log.info("[Jwt] doGenerateToken email: {}", email);
        log.info("[Jwt] doGenerateToken expireTime: {}", expireTime);

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS512");

        Claims claims = Jwts.claims();
        claims.put("email", email);

        return Jwts.builder()
            .setHeader(headers)
            .setClaims(claims)
            .setExpiration(new Date(System.currentTimeMillis() + expireTime))
            .signWith(getSignKey(), SignatureAlgorithm.HS512)
            .compact();
    }

    public Claims validTokenAndReturnBody(String token) {
        log.info("[Jwt] validTokenAndReturnBody token: {}", token);
        try {
            return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new InvalidParameterException("유효하지 않은 토큰입니다");
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("==> BearerToken : {}", bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer"))
            return bearerToken.substring(7);
        return "";
    }
}
