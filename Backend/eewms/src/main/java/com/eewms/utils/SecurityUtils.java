package com.eewms.utils;

import com.eewms.entities.Account;
import com.eewms.exception.InventoryException;
import com.eewms.exception.ExceptionMessage;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Configuration
@Slf4j
public class SecurityUtils {
    @Value("${jwt.signerKey}")
    private String signerKey;

    public String generate(Account account) throws InventoryException {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .expirationTime(Date.from(Instant.now().plus(4, ChronoUnit.HOURS)))
                .claim("role_id", account.getRoleId())
                .claim("account_id", account.getAccountId())
                .build();
        JWSObject jws = new JWSObject(
                new JWSHeader(JWSAlgorithm.HS256),
                new Payload(claims.toJSONObject())
        );
        try {
            jws.sign(new MACSigner(signerKey.getBytes()));
            return jws.serialize();
        } catch (JOSEException e) {
            log.error("JWT sign error", e);
            throw new InventoryException(
//                    ExceptionMessage.INTERNAL_SERVER_ERROR,
//                    ExceptionMessage.messages.get(ExceptionMessage.INTERNAL_SERVER_ERROR)
            );
        }
    }

    public String decode(String authHeader) {
        return authHeader.substring(7);
    }
}
