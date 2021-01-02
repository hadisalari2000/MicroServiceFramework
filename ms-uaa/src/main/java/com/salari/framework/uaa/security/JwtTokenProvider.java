package com.salari.framework.uaa.security;
import com.salari.framework.uaa.model.entity.User;
import com.salari.framework.uaa.model.enums.TokenTypes;
import com.salari.framework.uaa.utility.ApplicationProperties;
import io.jsonwebtoken.*;
import com.salari.framework.uaa.model.dto.user.JwtUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


@Component
@RefreshScope
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

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

    public Integer getUserIdFromJWT(String token) {
        //get jwt from token
        token = token.trim().replaceFirst("(?i)bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());
    }

    public String getSubjectFromJWT(String token) {
        //get jwt from token
        token = token.trim().replaceFirst("(?i)bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
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


