package com.bsmm.login.security;

import com.bsmm.login.domain.UserEntity;
import com.bsmm.login.service.dto.LoginResponse;
import com.bsmm.login.util.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        var secret = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(UserEntity user, Date now, long expiration) {
        var claimsBuilder = Jwts.claims().subject(user.getUsername());
        Collection<? extends GrantedAuthority> authorities = user.getRoles().stream().map(eRole -> new SimpleGrantedAuthority(eRole.name())).toList();
        if (!authorities.isEmpty()) {
            claimsBuilder.add(Constants.CLAIM_ROLES, authorities.stream().map(GrantedAuthority::getAuthority).toList());
        }
        claimsBuilder.add(Constants.CLAIM_EMAIL, user.getUsername());
        claimsBuilder.add(Constants.CLAIM_NAME, user.getFullName());
        return Jwts.builder()
                .claims(claimsBuilder.build())
                .issuedAt(now)
                .id(UUID.randomUUID().toString())
                .expiration(new Date(expiration))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

        List<String> stringList = (ArrayList<String>) claims.get(Constants.CLAIM_ROLES);

        Collection<? extends GrantedAuthority> authorities = stringList == null
                ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.createAuthorityList(stringList);

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public String getUserNameFromJwt(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(depureToken(token)).getBody().getSubject();
    }

    private String depureToken(String token) {
        if (token == null) {
            return null;
        }
        return token.replace(Constants.TOKEN_PREFIX, "");
    }

    public String getClaimId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(depureToken(token)).getBody().getId();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            log.info("expiration date: {}", claims.getPayload().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    public LoginResponse getResponse(UserEntity user) {
        long currentTime = System.currentTimeMillis();
        long expirationAT = currentTime + jwtProperties.getExpirationAt();
        long expirationRT = currentTime + jwtProperties.getExpirationRt();
        Date now = new Date();
        return new LoginResponse(Constants.TOKEN_TYPE,
                createToken(user, now, expirationAT),
                createToken(user, now, expirationRT),
                expirationAT);
    }
}
