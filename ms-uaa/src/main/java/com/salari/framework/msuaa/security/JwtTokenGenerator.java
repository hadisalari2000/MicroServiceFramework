package com.salari.framework.msuaa.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.salari.framework.msuaa.model.dto.user.JwtUserDTO;
import org.joda.time.DateTime;

import java.util.UUID;

public class JwtTokenGenerator {

    static String generateToken(JwtUserDTO u, String secret) {
        Claims claims = Jwts.claims().setSubject(u.getId().toString());
        claims.put("role", u.getRoles());
        claims.put("type", u.getType());
        claims.put("jti", u.getJti());
        claims.put("expiration", u.getExpiration());


        DateTime currentTime = new DateTime();
        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(currentTime.toDate())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public static String generateRefreshToken(JwtUserDTO u, String secret) {
        Claims claims = Jwts.claims().setSubject(u.getId().toString());
        claims.put("role", u.getRoles());
        claims.put("type", u.getType());
        claims.put("jti", u.getJti());
        DateTime currentTime = new DateTime();
        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(currentTime.toDate())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

}
