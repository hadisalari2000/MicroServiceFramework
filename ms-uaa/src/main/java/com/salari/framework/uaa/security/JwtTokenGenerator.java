package com.salari.framework.uaa.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.salari.framework.uaa.model.dto.user.JwtUserDTO;
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

    //public static void main(String[] args) {

//        JwtUserDTO user = new JwtUserDTO();
//        user.setRoles("admin");
//        user.setExpiration(new Date().getTime());
//        user.setJti(UUID.randomUUID().toString());
//        user.setType(ApplicationProperties.getInstance().getProperty("jwt.type"));
//
//        String jwt_token = generateToken(user, ApplicationProperties.getInstance().getProperty("secret"));
//        System.out.println("**************************************\n\n" + jwt_token + "\n\n**************************************");
//
//        JwtTokenValidator jwtTokenValidator = new JwtTokenValidator();
//
//        user = jwtTokenValidator.parseToken(jwt_token);
//        System.out.println(user.getRole());
//        System.out.println(user.getUserId());
//        System.out.println(user.getType());
//        System.out.println(user.getJti());
    //}
}
