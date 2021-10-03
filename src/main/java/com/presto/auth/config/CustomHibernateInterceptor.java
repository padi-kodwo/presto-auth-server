package com.presto.auth.config;



import com.presto.auth.domain.TenantContext;
import com.presto.auth.interfaces.TenantSupport;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class CustomHibernateInterceptor extends EmptyInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomHibernateInterceptor.class);

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof TenantSupport) {
            String tenantId = ((TenantSupport) entity).getTenantId();

            if ((tenantId == null) && (TenantContext.getCurrentTenant() != null)) {
                ((TenantSupport) entity).setTenantId(TenantContext.getCurrentTenant());
            }
        }
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) {
        if (entity instanceof TenantSupport) {
            String tenantId = ((TenantSupport) entity).getTenantId();

            if ((tenantId == null) && (TenantContext.getCurrentTenant() != null)) {
                ((TenantSupport) entity).setTenantId(TenantContext.getCurrentTenant());
            }
        }

        return false;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof TenantSupport) {
            for (int i = 0; i < propertyNames.length; i++) {
                if ("tenantId".equals(propertyNames[i])) {
                    Object tenantId = state[i];

                    if (tenantId == null) {
                        state[i] = TenantContext.getCurrentTenant();

                        return true;
                    }
                }
            }
        }
        return false;
    }
}


