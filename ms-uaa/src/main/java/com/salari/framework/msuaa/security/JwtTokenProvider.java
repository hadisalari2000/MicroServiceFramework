package com.salari.framework.msuaa.security;
import com.salari.framework.common.handler.exception.AuthorizeException;
import com.salari.framework.msuaa.model.enums.TokenTypes;
import com.salari.framework.msuaa.utility.ApplicationUtilities;
import io.jsonwebtoken.*;
import com.salari.framework.msuaa.model.dto.user.JwtUserDTO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.UUID;


@Component
@RefreshScope
public class JwtTokenProvider {

    //private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.expiration.time-minute}")
    private Integer tokenExpirationTime;

    @Value("${jwt.refresh.expiration.time-hour}")
    private Integer refreshExpirationTime;

    @Value("${jwt.password.expiration.time-minute}")
    private Integer passwordExpirationTime;

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
            //logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            //logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            //logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            //logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            //logger.error("JWT claims string is empty.");
        }
        return false;
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

    String getJwtTokenFromRequest(HttpServletRequest request) {

        if (request.getHeader("Authorization") == null)
            throw AuthorizeException.getInstance("unauthenticated_expired");

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken.isEmpty() || !bearerToken.startsWith("Bearer "))
            throw AuthorizeException.getInstance("unauthenticated_token");

        String jwt = bearerToken.substring(7);
        if (!validateToken(jwt)) {
            throw AuthorizeException.getInstance("unauthorized");
        }
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
        return jwt;
    }

}


