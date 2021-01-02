package com.salari.framework.uaa.security;
import com.salari.framework.uaa.model.entity.User;
import com.salari.framework.uaa.model.enums.TokenTypes;
import com.salari.framework.uaa.utility.ApplicationProperties;
import io.jsonwebtoken.*;
import com.salari.framework.uaa.model.dto.user.JwtUserDTO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


@Component
@RefreshScope
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.token.expiration.time-minute}")
    private Integer tokenExpirationTime;

    @Value("${jwt.refresh.expiration.time-hour}")
    private Integer refreshExpirationTime;

    @Value("${jwt.password.expiration.time-minute}")
    private Integer passwordExpirationTime;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private String generateToken(JwtUserDTO user, String secret) {
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("role", user.getRoleId());
        claims.put("type", user.getType());
        claims.put("jti", user.getJti());
        claims.put("expiration", user.getExpiration());

        DateTime currentTime = new DateTime();
        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(currentTime.toDate())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    Integer getUserIdFromJWT(String token) {
        token = token.trim().replaceFirst("(?i)bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }

    String getSubjectFromJWT(String token) {
        token = token.trim().replaceFirst("(?i)bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }


    public String generateJwtToken(Integer userId, Integer roleId, TokenTypes tokenTypes) {
        Calendar calendar = Calendar.getInstance();
        switch (tokenTypes)
        {
            case TOKEN:
                calendar.add(Calendar.MINUTE,tokenExpirationTime);
                break;
            case REFRESH:
                calendar.add(Calendar.HOUR,refreshExpirationTime);
                break;
            case PASSWORD:
                calendar.add(Calendar.MINUTE,passwordExpirationTime);
                break;
        }
        JwtUserDTO jwtUserDTO = JwtUserDTO.builder()
                .expiration(calendar.getTimeInMillis())
                .id(userId)
                .roleId(roleId)
                .jti(UUID.randomUUID().toString())
                .type(tokenTypes)
                .build();
        return generateToken(jwtUserDTO, jwtSecret);
    }

    public String generateJwtToken(User userAccount, String jwtType) {
        Calendar calendar = Calendar.getInstance();
        if (jwtType.equalsIgnoreCase(ApplicationProperties.getProperty("jwt.type.token"))) {
            calendar.add(Calendar.MINUTE, Integer.parseInt(ApplicationProperties.getProperty("jwt.expiration.time")));
        } else {
            calendar.add(Calendar.HOUR, Integer.parseInt(ApplicationProperties.getProperty("jwt.expiration.time.refresh")));
        }
        JwtUserDTO jwtUserDTO = JwtUserDTO.builder()
                .expiration(calendar.getTimeInMillis())
                .id(userAccount.getId())
                .jti(UUID.randomUUID().toString())
                .roles(userAccount.getRoles())
                .type(TokenTypes.valueOf(ApplicationProperties.getProperty("jwt.type")))
                .build();
        return JwtTokenGenerator.generateToken(jwtUserDTO, ApplicationProperties.getProperty("secret"));
    }
}


