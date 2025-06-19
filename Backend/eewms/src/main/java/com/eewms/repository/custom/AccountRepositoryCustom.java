package com.eewms.repository.custom;

import com.eewms.dto.AccountSearchDTO;
import com.eewms.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountRepositoryCustom {
    /**
     * Tìm kiếm động theo fullName và isBlock, có paging.
     */
    Page<Account> findAllBySearch(AccountSearchDTO searchDTO, Pageable pageable);
}
