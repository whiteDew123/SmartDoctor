package com.qst.medical.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String APP_SECRET = "medical_secret_key_2024";
    private static final long EXPIRE = 3600000L * 24;

    public static String getJwtToken(Long id, String uname, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("uname", uname);
        claims.put("role", role);

        String jwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("medical-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();

        return jwtToken;
    }

    public static Claims parseJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(APP_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean checkToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            Claims claims = parseJwtToken(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public static Long getId(String token) {
        Claims claims = parseJwtToken(token);
        return claims.get("id", Long.class);
    }

    public static String getUname(String token) {
        Claims claims = parseJwtToken(token);
        return claims.get("uname", String.class);
    }

    public static String getRole(String token) {
        Claims claims = parseJwtToken(token);
        return claims.get("role", String.class);
    }
}