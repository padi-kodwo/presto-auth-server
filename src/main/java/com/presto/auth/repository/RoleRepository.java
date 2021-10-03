package com.presto.auth.repository;

import com.presto.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, String>, JpaSpecificationExecutor<Role> {

    Optional<Role> findByCode(int code);

    Optional<Role> findByName(String name);
}