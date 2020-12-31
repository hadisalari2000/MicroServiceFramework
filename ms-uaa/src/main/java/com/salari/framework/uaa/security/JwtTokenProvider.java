package com.salari.framework.uaa.security;

import com.salari.framework.uaa.handler.exception.ServiceException;
import com.salari.framework.uaa.model.dto.user.JwtUserDTO;
import com.salari.framework.uaa.model.enums.TokenTypes;
import com.salari.framework.uaa.utility.ApplicationUtilities;
import io.jsonwebtoken.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.UUID;

@Component
@RefreshScope
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.token.expiration.time-minute}")
    private Integer tokenExpirationTime;

    @Value("${jwt.refresh.expiration.time-hour}")
    private Integer refreshExpirationTime;

    @Value("${jwt.password.expiration.time-minute}")
    private Integer passwordExpirationTime;

    @Value("${jwt.secret}")
    private String jwtSecret;

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
            default:
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

    public String generateToken(JwtUserDTO user, String secret) {
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

    public JwtUserDTO parseToken() {
        HttpServletRequest request = ApplicationUtilities.getInstance().getCurrentHttpRequest();
        String jwt = getJwtTokenFromRequest(request);
        JwtUserDTO jwtUserDTO = new JwtUserDTO();
        Claims body = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody();
        jwtUserDTO.setId(Integer.parseInt(body.getSubject()));
        jwtUserDTO.setRoleId((Integer) (body.get("role")));
        jwtUserDTO.setExpiration((Long) body.get("expiration"));
        jwtUserDTO.setType(TokenTypes.valueOf((String) body.get("type")));
        jwtUserDTO.setJti((String) body.get("jti"));
        return jwtUserDTO;
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        if (request.getHeader("Authorization") == null)
            throw ServiceException.getInstance("unauthenticated", HttpStatus.UNAUTHORIZED);
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken.isEmpty() || !bearerToken.startsWith("Bearer "))
            throw ServiceException.getInstance("unauthenticated_token", HttpStatus.UNAUTHORIZED);
        String jwt = bearerToken.substring(7);
        String exceptionKey = "unauthenticated_invalid";
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return jwt;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            exceptionKey = "unauthenticated_expired";
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        throw ServiceException.getInstance(exceptionKey, HttpStatus.UNAUTHORIZED);
    }
}
