package com.presto.auth.domain;

public class TenantContext {
    public static final String GOD_TENANT = "X-Crossfire";

    private static ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static void clear() {
        currentTenant.set(null);
    }

}
