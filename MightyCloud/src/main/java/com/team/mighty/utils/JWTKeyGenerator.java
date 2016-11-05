package com.team.mighty.utils;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.http.HttpStatus;

import com.team.mighty.exception.MightyAppException;
import com.team.mighty.logger.MightyLogger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTKeyGenerator {

	private static final MightyLogger logger = MightyLogger.getLogger(JWTKeyGenerator.class);
	
	public static void validateXToken(String xToken) throws MightyAppException {
		if (xToken == null) {
			throw new MightyAppException("Invalid XToken Value", HttpStatus.UNAUTHORIZED);
		}
	}

	public static String createJWTToken(String serviceInvokerKey, String id, String subject, long ttlMillis) {

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(serviceInvokerKey);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		String issuerName = "SHAN";
		//String issuerName = SpringPropertiesUtil.getProperty(MightyAppConstants.TOKEN_ISSUER_KEY);
		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setIssuer(issuerName)
				.signWith(signatureAlgorithm, signingKey);

		// if it has been specified, let's add the expiration
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}

	public static void validateJWTToken(String serviceInvokerKey, String jwt) throws MightyAppException {

		// This line will throw an exception if it is not a signed JWS (as
		// expected)
		try {
			Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(serviceInvokerKey))
					.parseClaimsJws(jwt).getBody();
			logger.info("ID: " + claims.getId());
			logger.info("Subject: " + claims.getSubject());
			logger.info("Issuer: " + claims.getIssuer());
			logger.info("Expiration: " + claims.getExpiration());
		} catch(Exception e) {
			throw new MightyAppException("X-Mighty-token Value Expired", HttpStatus.UNAUTHORIZED);
		}
		
		
	}

	public static void main(String[] args) {
		String token = createJWTToken("MYSECRETKEY", "ID", "SUB", 500);
		validateJWTToken("MYSECRETKEY", token);
	}

}
