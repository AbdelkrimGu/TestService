package com.example.userservice.Generators;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class TokenGenerator {

    @Value("${app.security.key}")
    private String secretKey;

    private final String secondKey = "J0m0+pb9ZyzmUvCvGom6PQEC6AJxpw50vgbY6rZz6ws=";

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final long EXPIRATION_TIME = 3600000 * 24; // 24 hour

    /*public String encrypt(String plainText) throws Exception {
        System.out.println(secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }*/
    public String encrypt(String email) {
        UUID uuid = UUID.randomUUID(); // generate a unique UUID
        String token = uuid.toString().substring(0, 5); // use the first 5 characters of the UUID as the token
        return token;
    }

    public String decrypt(String encryptedText) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


    public String generateToken(String subject) {
        // Set the token expiration date/time
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        // Set the token claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);
        claims.put("exp", expirationDate);

        // Generate the JWT token
        return Jwts.builder()
                .setClaims(claims)
                .signWith(Keys.hmacShaKeyFor(secondKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String verifyToken(String token) throws Exception {
        // Parse the JWT token and extract the claims
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secondKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Check if the token has expired
        Date expirationDate = claims.getExpiration();
        if (expirationDate.before(new Date())) {
            throw new Exception("Token has expired");
        }

        System.out.println(claims.getSubject());

        // Return the subject (i.e., user ID) of the token
        return claims.getSubject();
    }
}
