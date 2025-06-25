package com.example.demo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.dto.user.UserResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;


@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    @Value("${security.jwt.token.secret-secret-key:secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        // This method can be used to initialize any resources or configurations needed for the UserAuthProvider
       secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UserResponseDto user) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // Token valid for 1 hour

       return JWT.create()
                .withSubject(user.getUserName())
                .withClaim("id", user.getId().toString())
                .withClaim("email", user.getEmail())
               .withIssuedAt(now)
               .withExpiresAt(validity) // Token valid for 1 hour
                .sign(com.auth0.jwt.algorithms.Algorithm.HMAC256(secretKey)) ;// Use HMAC256 algorithm with the secret key
    }

    public Authentication validateToken(String token) {

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm)
                .build(); // Reusable verifier instance

        DecodedJWT decoded = verifier.verify(token);

        UserResponseDto user =  UserResponseDto.builder().login(decoded.getIssuer()).email(decoded.getClaim("email").asString())
                .id(UUID.fromString(decoded.getClaim("id").asString()))
                .userName(decoded.getSubject())
                .build();

        return new UsernamePasswordAuthenticationToken(user, null,  Collections.emptyList());
    }
}
