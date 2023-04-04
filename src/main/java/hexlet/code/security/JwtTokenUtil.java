package hexlet.code.security;

import hexlet.code.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    private final String secretKey;
    private final String issuer;
    private final Long expirationSecond;

    public JwtTokenUtil(
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.expireSeconds}") Long expirationSecond) {
        this.secretKey = secretKey;
        this.issuer = issuer;
        this.expirationSecond = expirationSecond;
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationSecond * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validate(String token) {
        return isExpiredToken(token);
    }

    public String getUserName(String token) {
        return getAllClaimsForToken(token).getSubject();
    }

    private boolean isExpiredToken(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return new Date().before(expirationDate);
    }

    private Claims getAllClaimsForToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Date getExpirationDateFromToken(String token) {
        return getAllClaimsForToken(token).getExpiration();
    }
}
