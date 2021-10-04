package com.presto.auth.repository;

import com.presto.auth.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String>, JpaSpecificationExecutor<User> {

    Optional<User> findById(String id);

    Optional<User> findUserByEmail(String email);

    Optional<User> findByPhone(String phone);

    @Query("SELECT u FROM User u where u.phone = :phone OR (u.email is not null AND u.email = :email)")
    Set<User> findByPhoneOrEmail(String phone, String email);


    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.code = :roleCode")
    Set<User> findByRoleCode(String roleCode);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    @Query("SELECT u from User u left join u.roles r where r.name = ?1 ")
    Set<User> findByRole(String roleName);

    Long countAllByLoggedInIsTrue();
}