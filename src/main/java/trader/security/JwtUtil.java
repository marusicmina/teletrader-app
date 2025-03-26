package trader.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {
    private String secretKey = "JWT_SECRET_KEY";  // Koristi siguran ključ sa okruženja

    // Metoda za generisanje tokena
    public String generateToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // Token važi 1 sat
                .sign(algorithm);
    }

    // Metoda za verifikaciju tokena
    public DecodedJWT verifyToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
    public String extractUsername(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))  // Koristite isti algoritam kao kod generisanja
                .build()
                .verify(token)
                .getClaim("sub")  // Pretpostavka da se username čuva u claimu "sub"
                .asString();
    }

}