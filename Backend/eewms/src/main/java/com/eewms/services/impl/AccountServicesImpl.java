package com.eewms.services.impl;

import com.eewms.exception.ExceptionMessage;
import com.eewms.constant.RoleEnum;
import com.eewms.dto.AccountSearchDTO;
import com.eewms.dto.AccountUpdateDTO;
import com.eewms.dto.AccountPasswordUpdateDTO;
import com.eewms.entities.Account;
import com.eewms.exception.InventoryException;
import com.eewms.repository.AccountRepository;
import com.eewms.repository.custom.AccountRepositoryCustom;
import com.eewms.services.IAccountServices;
import com.eewms.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jose.JwaAlgorithm;
import javax.crypto.spec.SecretKeySpec;

@Service
@RequiredArgsConstructor
public class AccountServicesImpl implements IAccountServices {
    private final AccountRepository accountRepo;
    private final AccountRepositoryCustom accountRepositoryCustom; // * custom repo *
    private final SecurityUtils securityUtils;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Override
    public Page<Account> getAll(String authHeader,
                                Pageable pageable,
                                AccountSearchDTO dto)
            throws InventoryException {
        Account me = getFullInformation(authHeader);

        if (me.getRoleId() != RoleEnum.ADMIN.ordinal()) {
            throw new InventoryException(
//                    ExceptionMessage.NO_PERMISSION,
//                    ExceptionMessage.messages.get(ExceptionMessage.NO_PERMISSION)
            );
        }
        return accountRepositoryCustom.findAllBySearch(dto, pageable);
    }

    @Override
    public Account getFullInformation(String authHeader) {
        String token = securityUtils.decode(authHeader);
        SecretKeySpec keySpec = new SecretKeySpec(signerKey.getBytes(), "HS256");
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
                .withSecretKey(keySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        Jwt jwt = jwtDecoder.decode(token);

        Integer accountId = ((Number) jwt.getClaims().get("account_id")).intValue();
        return accountRepo.findById(accountId)
                .orElseThrow(
//                        ()
//                        -> new InventoryException(
//                        ExceptionMessage.NO_PERMISSION,
//                        ExceptionMessage.messages.get(ExceptionMessage.NO_PERMISSION)
//                )
                );

    }
    @Override
    public Account findByUsername(String authHeader, String username)
            throws InventoryException {
        Account me = getFullInformation(authHeader);
        if (me.getRoleId() != RoleEnum.ADMIN.ordinal()
                && !me.getUsername().equals(username)) {
            throw new InventoryException(
//                    ExceptionMessage.NO_PERMISSION,
//                    ExceptionMessage.messages.get(ExceptionMessage.NO_PERMISSION)
            );
        }
        return accountRepo.findByUsername(username)
                .orElseThrow(
//                        () -> new InventoryException(
//                        ExceptionMessage.ACCOUNT_NOT_FOUND,
//                        ExceptionMessage.messages.get(ExceptionMessage.ACCOUNT_NOT_FOUND)
//                )
        );

    }

    @Override
    public void updateByUsername(String authHeader,
                                 String username,
                                 AccountUpdateDTO dto)
            throws InventoryException {
        Account me = getFullInformation(authHeader);
        if (me.getRoleId() != RoleEnum.ADMIN.ordinal()) {
            throw new InventoryException(
//                    ExceptionMessage.NO_PERMISSION,
//                    ExceptionMessage.messages.get(ExceptionMessage.NO_PERMISSION)
            );
        }
        Account target = accountRepo.findByUsername(username)
                .orElseThrow(
//                        () -> new InventoryException(
//                        ExceptionMessage.ACCOUNT_NOT_FOUND,
//                        ExceptionMessage.messages.get(ExceptionMessage.ACCOUNT_NOT_FOUND)
//                )
                );
        target.setFullName(dto.getFullName());
        target.setMobile(dto.getMobile());
        target.setRoleId(dto.getRoleId());
        accountRepo.save(target);
    }

    @Override
    public void lockAccount(String authHeader, String username)
            throws InventoryException {
        Account me = getFullInformation(authHeader);
        if (me.getRoleId() != RoleEnum.ADMIN.ordinal()) {
            throw new InventoryException(
//                    ExceptionMessage.NO_PERMISSION,
//                    ExceptionMessage.messages.get(ExceptionMessage.NO_PERMISSION)
            );
        }
        Account target = accountRepo.findByUsername(username)
                .orElseThrow(
//                        () -> new InventoryException(
//                        ExceptionMessage.ACCOUNT_NOT_FOUND,
//                        ExceptionMessage.messages.get(ExceptionMessage.ACCOUNT_NOT_FOUND)
//                )
                );
        target.setIsBlock(true);
        accountRepo.save(target);
    }

    @Override
    public void unlockAccount(String authHeader, String username)
            throws InventoryException {
        Account me = getFullInformation(authHeader);
        if (me.getRoleId() != RoleEnum.ADMIN.ordinal()) {
            throw new InventoryException(
//                    ExceptionMessage.NO_PERMISSION,
//                    ExceptionMessage.messages.get(ExceptionMessage.NO_PERMISSION)
            );
        }
        Account target = accountRepo.findByUsername(username)
                .orElseThrow(
//                        () -> new InventoryException(
//                        ExceptionMessage.ACCOUNT_NOT_FOUND,
//                        ExceptionMessage.messages.get(ExceptionMessage.ACCOUNT_NOT_FOUND)
//                )
                );
        target.setIsBlock(false);
        accountRepo.save(target);
    }

    @Override
    public void updatePasswordForAccount(String authHeader,
                                         String username,
                                         AccountPasswordUpdateDTO dto)
            throws InventoryException {
        Account me = getFullInformation(authHeader);
        Account target = accountRepo.findByUsername(username)
                .orElseThrow(
//                        () -> new InventoryException(
//                        ExceptionMessage.ACCOUNT_NOT_FOUND,
//                        ExceptionMessage.messages.get(ExceptionMessage.ACCOUNT_NOT_FOUND)
//                )
                );

        if (me.getRoleId() != RoleEnum.ADMIN.ordinal()) {
            if (!me.getUsername().equals(username)) {
                throw new InventoryException(
//                        ExceptionMessage.NO_PERMISSION,
//                        ExceptionMessage.messages.get(ExceptionMessage.NO_PERMISSION)
                );
            }
            if (!passwordEncoder.matches(dto.getOldPassword(), me.getPassword())) {
                throw new InventoryException(
//                        ExceptionMessage.UPDATE_PASSWORD_FAIL,
//                        ExceptionMessage.messages.get(ExceptionMessage.UPDATE_PASSWORD_FAIL)
                );
            }
        }

        target.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        accountRepo.save(target);
    }
}
