package com.presto.auth.exception;

public class TenantException extends RuntimeException {

    private String tenant;

    public TenantException(String tenant){
        this.tenant = tenant;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    @Override
    public String getMessage() {
        return "No or invalid tenant: " + tenant;
    }

}
