package com.presto.auth.service.aspect;

import com.presto.auth.domain.TenantContext;
import com.presto.auth.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserServiceAspect {

    @Before("execution(* com.presto.auth.service.impl.UserServiceImpl.*(..)) && !execution(* com.presto.auth.service.impl.UserServiceImpl.run(..)) && !execution(* com.presto.auth.service.impl.UserServiceImpl.loadUserByUsername(..)) && target(userService)")
    public void aroundExecution(JoinPoint pjp, UserServiceImpl userService) {

        String tenant = TenantContext.getCurrentTenant();

        if (StringUtils.isNotEmpty(tenant)) {
            if (tenant.equalsIgnoreCase("X-Crossfire")) return;

            org.hibernate.Filter filter = userService.entityManager.unwrap(Session.class).enableFilter("tenantFilter");
            filter.setParameter("tenantId", TenantContext.getCurrentTenant());
            filter.validate();
        }
    }
}



