package com.presto.auth.repository;

import com.presto.auth.entity.AccountValidationToken;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountValidationTokenRepository extends PagingAndSortingRepository<AccountValidationToken, String> {
}
