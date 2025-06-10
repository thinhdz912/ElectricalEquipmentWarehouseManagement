package com.eewms.services.impl;

import com.eewms.dto.AuthenDTO;
import com.eewms.dto.RegisterDTO;
import com.eewms.dto.response.AuthenResponseDTO;
import com.eewms.entities.Account;
import com.eewms.exception.InventoryException;
import com.eewms.repository.AccountRepository;
import com.eewms.services.IAuthenticatedServices;
import com.eewms.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
    public class AuthenticatedServicesImpl implements IAuthenticatedServices {
        private final AccountRepository accountRepo;
        private final PasswordEncoder passwordEncoder;
        private final SecurityUtils securityUtils;

        @Override
        public AuthenResponseDTO login(AuthenDTO dto) throws InventoryException {
            Account acc = accountRepo.findByUsername(dto.getUsername())
                    .orElseThrow(() -> new InventoryException(
//                            ExceptionMessage.USERNAME_INCORRECT,
//                            ExceptionMessage.messages.get(ExceptionMessage.USERNAME_INCORRECT)
                    ));
            if (!acc.getRoleId().equals(acc.getRoleId())) {} // just placeholder if you have block flag
            boolean ok = passwordEncoder.matches(dto.getPassword(), acc.getPassword());
            if (!ok) {
                throw new InventoryException(
//                        ExceptionMessage.USERNAME_INCORRECT,
//                        ExceptionMessage.messages.get(ExceptionMessage.USERNAME_INCORRECT)
                );
            }
            String token = securityUtils.generate(acc);
            return AuthenResponseDTO.builder()
                    .token(token)
                    .info(acc)
                    .build();
        }

        @Override
        public void register(RegisterDTO dto) throws InventoryException {
            if (accountRepo.existsByUsername(dto.getUsername())) {
                throw new InventoryException(
//                        ExceptionMessage.??,
//                        ExceptionMessage.messages.get(ExceptionMessage.EMPLOYEE_EXISTED)
                );
            }
            Account acc = Account.builder()
                    .fullName(dto.getFullName())
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .email(dto.getEmail())
                    .mobile(dto.getMobile())
                    .roleId(dto.getRoleId())
                    .build();
            accountRepo.save(acc);
        }
    }


