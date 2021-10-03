package com.presto.auth.interfaces;

public interface TenantSupport {
    String getTenantId();
    void setTenantId(String tenantId);
}
