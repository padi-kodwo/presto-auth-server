package com.presto.auth.service.impl;


import com.presto.auth.entity.Role;
import com.presto.auth.exception.ServiceException;
import com.presto.auth.repository.RoleRepository;
import com.presto.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RefreshScope
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public Role getRole(int code) {
    return roleRepository.findByCode(code).orElseThrow(()->
                new ServiceException(102, "no role found for: " + code));
    }

    @Override
    @Transactional
    public Page<Role> getAllRoles(Specification<Role> spec, Pageable pageable) {
        return roleRepository.findAll(spec, pageable);
    }
}
