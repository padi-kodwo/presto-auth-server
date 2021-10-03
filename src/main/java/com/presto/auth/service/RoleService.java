package com.presto.auth.service;

import com.presto.auth.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RoleService {
    Role getRole(int code);

    Page<Role> getAllRoles(Specification<Role> spec, Pageable pageable);
}
