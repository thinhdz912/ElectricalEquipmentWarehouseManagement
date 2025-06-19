package com.eewms.repository.custom.impl;

import com.eewms.dto.AccountSearchDTO;
import com.eewms.entities.Account;
import com.eewms.repository.custom.AccountRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AccountRepositoryCustomImpl implements AccountRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Account> findAllBySearch(AccountSearchDTO searchDTO, Pageable pageable) {
        // 1) Xây SQL chính và SQL count
        StringBuilder sql      = new StringBuilder("SELECT * FROM account WHERE 1=1");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(1) FROM account WHERE 1=1");
        Map<String,Object> params = new HashMap<>();

        // 2) Thêm điều kiện WHERE (fullName, isBlock)
        addingWhereClause(sql,      searchDTO, params);
        addingWhereClause(sqlCount, searchDTO, params);

        // 3) Thêm paging
        addingPaging(sql, pageable);

        // 4) Tạo query và set parameters
        Query qData  = entityManager.createNativeQuery(sql.toString(), Account.class);
        Query qCount = entityManager.createNativeQuery(sqlCount.toString());
        setParameters(qData, params);
        setParameters(qCount, params);

        @SuppressWarnings("unchecked")
        var list = qData.getResultList();
        long total = ((Number) qCount.getSingleResult()).longValue();

        // 5) Trả về Page
        return new PageImpl<>(list, pageable, total);
    }

    private void addingWhereClause(StringBuilder sql,
                                   AccountSearchDTO dto,
                                   Map<String,Object> params) {
        if (StringUtils.isNotBlank(dto.getFullName())) {
            sql.append(" AND full_name LIKE :fullName");
            params.put("fullName", "%" + dto.getFullName() + "%");
        }
        if (dto.getIsBlock() != null) {
            sql.append(" AND is_block = :isBlock");
            params.put("isBlock", dto.getIsBlock());
        }
    }

    private void addingPaging(StringBuilder sql, Pageable pageable) {
        sql.append(" LIMIT ")
                .append(pageable.getPageSize())
                .append(" OFFSET ")
                .append(pageable.getOffset());
    }

    private void setParameters(Query q, Map<String,Object> params) {
        params.forEach(q::setParameter);
    }
}
