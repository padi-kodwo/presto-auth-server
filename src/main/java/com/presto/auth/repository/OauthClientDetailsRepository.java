package com.presto.auth.repository;

import com.presto.auth.entity.OauthClient;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthClientDetailsRepository extends PagingAndSortingRepository<OauthClient, String>, JpaSpecificationExecutor<OauthClient> {

    Optional<OauthClient> findById(String id);
}