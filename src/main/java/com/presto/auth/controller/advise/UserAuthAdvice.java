package com.presto.auth.controller.advise;

import com.presto.auth.domain.TenantContext;
import com.presto.auth.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestControllerAdvice(basePackages = {"com.presto.auth.controller"})
public class UserAuthAdvice {
    private static final Logger logger = LoggerFactory.getLogger(UserAuthAdvice.class);
    private static final String TENANT_HEADER = "X-Tenant-Id";

    @ModelAttribute
    void authUser(HttpServletRequest request, Authentication auth) {
        String tenantHeader = request.getHeader(TENANT_HEADER);

        if (!StringUtils.isEmpty(tenantHeader)) {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            boolean hasServiceAuthority = authorities.stream().anyMatch(grantedAuthority ->
                    grantedAuthority.getAuthority().equalsIgnoreCase("PRESTO_SERVICE") ||
                            grantedAuthority.getAuthority().equalsIgnoreCase("PRESTO_APP") ||
                            grantedAuthority.getAuthority().equalsIgnoreCase("ROLE_USER"));

            if (tenantHeader.equalsIgnoreCase("X-Crossfire") && !hasServiceAuthority)
                throw new ServiceException(100, "unauthorized tenant context");

            TenantContext.setCurrentTenant(tenantHeader);
        } else {
            throw new ServiceException(100, "missing tenant context");
        }
    }
}
