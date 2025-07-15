package com.ecommerce.eCom.security.jwt;


import com.ecommerce.eCom.security.service.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @Value("${spring.app.secretKey}")
    private String secretKey;

    @Value("${spring.ecom.app.jwtCookieName}")
    private String jwtCookie;

    /*
    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken !=null && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }*/

    public String getJwtCookie(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request,jwtCookie);

        if(cookie != null){
            return cookie.getValue();
        }else{
            return null;
        }

    }


    public ResponseCookie generateJWTCookie(UserDetailsImpl userDetails){
        String jwt = generateTokenFromUsername(userDetails.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie,jwt)
                .path("/api")
                .maxAge(24 * 60 * 60)
                .httpOnly(false)
                .build();
        return cookie;
    }

    public String generateTokenFromUsername(String userName){
        //String userName = userDetails.getUsername();
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtExpirationInMs)))
                .signWith(key())
                .compact();

    }

    public String getUserNameFromJWTToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();

    }

    private Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secretKey)
        );

    }

    // Validate JWT Token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT Token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT Token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT Token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
