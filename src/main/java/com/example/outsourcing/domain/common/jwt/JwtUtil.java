package com.example.outsourcing.domain.common.jwt;

import com.example.outsourcing.domain.user.enums.UserRole;
import com.example.outsourcing.domain.common.exception.ServerException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";

    private static final long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000L; // 1시간
    private static final long REFRESH_TOKEN_EXPIRATION = 14 * 24 * 60 * 60 * 1000L; // 14일

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(Long userId, String email, UserRole userRole) {
        return generateToken(userId, email, userRole, ACCESS_TOKEN_EXPIRATION, "access");
    }

    public String createRefreshToken(Long userId) {
        return generateToken(userId, null, null, REFRESH_TOKEN_EXPIRATION, "refresh");
    }

    public String generateToken(Long userId, String email, UserRole userRole, long expirationTime, String type) {
        Date now = new Date();
        log.info("userRole : '{}'",userRole );
        JwtBuilder builder = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(now.getTime() + expirationTime))
                .setIssuedAt(now)
                .claim("type", type);

        if (email != null) builder.claim("email", email);
        if (userRole != null) builder.claim("userRole", userRole);

        log.info("userRole : '{}'", userRole); // null이 아닌 경우만 찍힘

        return builder.signWith(key, signatureAlgorithm).compact();
    }


    public String substringToken(String tokenValue) {
        log.info("Received token: '{}'", tokenValue);

        if (!StringUtils.hasText(tokenValue)) {
            log.error("Token is empty or null.");
            throw new ServerException("토큰이 비어 있습니다.");
        }

        if (tokenValue.startsWith(BEARER_PREFIX)) {
            String extractedToken = tokenValue.substring(BEARER_PREFIX.length());
            log.info("Extracted token: '{}'", extractedToken);
            return extractedToken;
        } else {
            log.warn("Token does not start with 'Bearer '. Assuming raw token format.");
            return tokenValue; // Bearer 없이 들어온 경우 그대로 반환
        }
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    public String getEmailFromToken(String token) {
        return extractClaims(token).get("email", String.class);
    }

    public UserRole getUserRoleFromToken(String token) {
        return UserRole.valueOf(extractClaims(token).get("userRole", String.class));
    }

    public boolean isAccessToken(String token) {
        return "access".equals(extractClaims(token).get("type", String.class));
    }

    public boolean isRefreshToken(String token) {
        Claims claims = extractClaims(token);

        // 로그 추가해서 "type" 값 확인
        String tokenType = claims.get("type", String.class);
        log.info("Extracted token type: {}", tokenType);

        return "refresh".equals(tokenType);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
            throw new ServerException("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new ServerException("유효하지 않은 토큰입니다.");
        }
    }
}
