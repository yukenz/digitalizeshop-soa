package id.co.awan.digitalizeshopsoa.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.CompressionAlgorithm;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import static id.co.awan.digitalizeshopsoa.config.VariableConfig.B2B2P_CLAIM_PRINCIPAL;
import static id.co.awan.digitalizeshopsoa.config.VariableConfig.B2B_ID;


@Service
public class JWTService {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final CompressionAlgorithm compressionAlgorithm;
    private final MessageDigest messageDigest;
    private final String issuer;
    private final String keyId;

    @Autowired
    public JWTService(
            ResourceLoader resourceLoader
    ) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {

//        // GENRSA
//        KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
//        rsa.initialize(2048);
//        KeyPair keyPair = rsa.generateKeyPair();
//
//        this.privateKey = keyPair.getPrivate();
//        this.publicKey = keyPair.getPublic();

        // Load Instance Crypto
        Resource resource = resourceLoader.getResource("classpath:key/server.pkcs8");
        KeyFactory rsaFactory = KeyFactory.getInstance("RSA");
        this.messageDigest = MessageDigest.getInstance("SHA-256");

        //LoadKey Private Key RSA PKCS8
        byte[] pkcs8Base64Byte = resource.getContentAsByteArray();
        byte[] pkcs8Byte = Base64.getDecoder().decode(pkcs8Base64Byte);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(pkcs8Byte);
        this.privateKey = rsaFactory.generatePrivate(pkcs8EncodedKeySpec);

        // LoadKey Public Key RSA
        java.security.interfaces.RSAPrivateCrtKey
                rsaPrivateKey = (java.security.interfaces.RSAPrivateCrtKey) privateKey;
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
                rsaPrivateKey.getModulus(),
                rsaPrivateKey.getPublicExponent());
        this.publicKey = rsaFactory.generatePublic(publicKeySpec);

        // COMPRESSIOON
//        this.compressionAlgorithm = Jwts.ZIP.GZIP;
        this.compressionAlgorithm = Jwts.ZIP.DEF;
//        this.compressionAlgorithm = new LZ4CompressionAlgorithm();

        // PROP
        this.issuer = "SOA";
        this.keyId = "ssos";
    }

    // ================ Core Function
    public String generateJWSWithPrivateKey(
            String sub,
            String aud,
            String b2bId,
            int expiredSecond
    ) {

        Instant nowInstant = Instant.now();
        return Jwts.builder()
                .header()
                .keyId(keyId).and()
                .audience().add(Collections.of(aud)).and()
                .subject(sub)
                .claim(B2B_ID, b2bId)
                .issuer(issuer)
                .issuedAt(Date.from(nowInstant))
                .expiration(Date.from(nowInstant.plus(expiredSecond, ChronoUnit.SECONDS)))
                .compressWith(compressionAlgorithm)
                .signWith(privateKey, Jwts.SIG.RS512)
                .compact();
    }

    public String generateJWSWithSecretKey(
            String sub,
            String aud,
            String username,
            String principalCredential,
            int expiredSecond
    ) {

        Instant nowInstant = Instant.now();
        byte[] secretKey = messageDigest.digest(principalCredential.getBytes());

        return Jwts.builder()
                .header()
                .keyId(keyId).and()
                .audience().add(Collections.of(aud)).and()
                .subject(sub)
                .claim(B2B2P_CLAIM_PRINCIPAL, username)
                .issuer(issuer)
                .issuedAt(Date.from(nowInstant))
                .expiration(Date.from(nowInstant.plus(expiredSecond, ChronoUnit.SECONDS)))
                .compressWith(compressionAlgorithm)
                .signWith(Keys.hmacShaKeyFor(secretKey), Jwts.SIG.HS256)
                .compact();
    }

    public Jws<Claims> parseJWSWithPublicKey(String jws) {
        return Jwts.parser()
                .zip().add(compressionAlgorithm)
                .and()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(jws);
    }

    public Jws<Claims> parseJWSWithSecrekKey(String jws, String backendSecret) {

        byte[] secretKey = messageDigest.digest(backendSecret.getBytes());
        return Jwts.parser()
                .zip().add(compressionAlgorithm)
                .and()
                .verifyWith(Keys.hmacShaKeyFor(secretKey))
                .build()
                .parseSignedClaims(jws);
    }


    // ================ Helper Function
    public String generateB2BToken(String b2bId, int expiredSecond) {
        return this.generateJWSWithPrivateKey("access_token", "b2b", b2bId, expiredSecond);
    }

    public String generateB2B2PToken(String username, String backendSecret, int expiredSecond) {
        return this.generateJWSWithSecretKey("access_token", "b2b2p", username, backendSecret, expiredSecond);
    }


}
