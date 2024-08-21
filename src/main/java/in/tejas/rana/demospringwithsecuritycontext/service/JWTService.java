package in.tejas.rana.demospringwithsecuritycontext.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Getter
@Setter
@Service
public class JWTService {

    static Log logger = LogFactory.getLog(JWTService.class);

    @Value("${jwt.privateKey.path:}")
    public String privateKeyPath;

    @Value("${jwt.publicKey.path:}")
    public String publicKeyPath;

    private PrivateKey privateKey;

    private PublicKey publicKey;

    @PostConstruct
    void init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        logger.info("Initializing JWTService");
        logger.info("Loading Private RSA key pair from " + privateKeyPath);
        logger.info("Loading Public RSA key pair from " + publicKeyPath);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Private Key
        byte[] keyBytes = Files.readAllBytes(Paths.get(privateKeyPath));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        privateKey = keyFactory.generatePrivate(spec);

        // Public Key
        if (StringUtils.isNotEmpty(publicKeyPath)) {
            String publicKeyContent = new String(Files.readAllBytes(Paths.get(publicKeyPath)));
            publicKeyContent = publicKeyContent
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "").trim();
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyContent);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            publicKey = keyFactory.generatePublic(publicKeySpec);
        }
    }

    public Claims decodeClaims(String token) {
        Claims claims = Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).getPayload();
        if (claims == null || claims.isEmpty()) {
            logger.error("INVALID TOKEN");
            throw new JwtException("Invalid Token");
        }
        return claims;
    }

    public String encodeClaims(Map<String, Object> claims, String subject, Integer expiryInMinutes, String audience) {

        if (null == subject || Objects.equals(subject, "")) {
            logger.error("Invalid token subject missing");
            throw new JwtException("Invalid Token");
        }
        if (null == expiryInMinutes || expiryInMinutes <= 0) {
            logger.error("Invalid token time expiry " + (expiryInMinutes == null ? "null" : expiryInMinutes));
            throw new JwtException("Invalid token time expiry");
        }

        long issueTime = System.currentTimeMillis();
        long expiryTime = issueTime + expiryInMinutes * 60 * 1000L;
        Date issuedAt = new Date(issueTime);
        Date expiry = new Date(expiryTime);


        return Jwts.builder()
                .claims(claims)
                .audience().add(audience).and()
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expiry)
                .signWith(privateKey, Jwts.SIG.RS256)
                .id(UUID.randomUUID().toString())
                .compact();
    }


}
