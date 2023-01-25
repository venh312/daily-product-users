package com.daily.product.users.jwt;

import com.daily.product.users.common.UserCookie;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
JWT(Json Web Token)란 Json 포맷을 이용하여 사용자에 대한 속성을 저장하는 Claim 기반의 Web Token이다. */
@Slf4j
@Component
public class TokenProvider {
    // 30분
    private final long ACCESS_TOKEN_SECOND = 60 * 30;
    // 7일
    private final long REFRESH_TOKEN_SECOND = 60 * 60 * 24 * 7;
    private final String secretKey;
    private final UserCookie userCookie;

    public TokenProvider(@Value("${global.jwt.key}") String secretKey, UserCookie userCookie) {
        this.secretKey = secretKey;
        this.userCookie = userCookie;
    }

    private Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public HashMap<String, String> generateToken(HttpServletResponse response, Long id) {
        HashMap<String, String> refreshTokenMap = generateRefreshToken(id);
        String accessToken = generateAccessToken(id);
        String refreshToken = refreshTokenMap.get("token");

        userCookie.setAccessToken(response, accessToken);
        userCookie.setRefreshToken(response, refreshToken);

        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("accessToken", accessToken);
        resultMap.put("refreshToken", refreshToken);
        resultMap.put("expiration", refreshTokenMap.get("expiration"));
        return resultMap;
    }

    public String generateAccessToken(Long id) {
        return doGenerateToken(id, ACCESS_TOKEN_SECOND * 1000l);
    }

    public HashMap<String, String> generateRefreshToken(Long id) {
        HashMap<String, String> hMap = new HashMap<>();
        hMap.put("token", doGenerateToken(id, REFRESH_TOKEN_SECOND * 1000l));
        hMap.put("expiration", String.valueOf(REFRESH_TOKEN_SECOND));
        return hMap;
    }

    public Long getId(String token) {
        return validTokenAndReturnBody(token).get("id", Long.class);
    }
	
	// 토큰 생성
    private String doGenerateToken(Long id, Long expireTime) {
        log.info("[Jwt] doGenerateToken email: {}", id);
        log.info("[Jwt] doGenerateToken expireTime: {}", expireTime);

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Claims claims = Jwts.claims();
        claims.put("id", id);

        return Jwts.builder()
            .setHeader(headers)
            .setClaims(claims)
            .setExpiration(new Date(System.currentTimeMillis() + expireTime))
            .signWith(getSignKey(), SignatureAlgorithm.HS512)
            .compact();
    }

    public Claims validTokenAndReturnBody(String token) {
        log.info("[Jwt] validTokenAndReturnBody token: {}", token);
        return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("==> BearerToken : {}", bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer"))
            return bearerToken.substring(7);
        return "";
    }
}
