package com.team.mighty.utils;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.team.mighty.constant.MightyAppConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTKeyGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String token = new JWTKeyGenerator().createJWTToken("MIGHTY_MOBILE","ID",  "SUBJECT", 500);

		System.out.println(token);

		try {

			// sleep 5 seconds
			Thread.sleep(2000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		new JWTKeyGenerator().parseJWT("MIGHTY_MOBILE",token);

	}

	public static String createJWTToken(String serviceInvokerKey, String id,  String subject, long ttlMillis) {

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(serviceInvokerKey);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		String issuerName = SpringPropertiesUtil.getProperty(MightyAppConstants.TOKEN_ISSUER_KEY);
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

	private void parseJWT(String serviceInvokerKey, String jwt) {

		// This line will throw an exception if it is not a signed JWS (as
		// expected)
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(serviceInvokerKey))
				.parseClaimsJws(jwt).getBody();
		System.out.println("ID: " + claims.getId());
		System.out.println("Subject: " + claims.getSubject());
		System.out.println("Issuer: " + claims.getIssuer());
		System.out.println("Expiration: " + claims.getExpiration());

		System.out.println(new Date().toString());
	}

	public String getSecret(String serviceInvoker) {

		return "MYSECRETKEY";

	}

}
