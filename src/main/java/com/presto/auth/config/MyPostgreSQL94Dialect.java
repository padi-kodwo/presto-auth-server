package com.presto.auth.config;

import org.hibernate.dialect.PostgreSQL94Dialect;

import java.sql.Types;

public class MyPostgreSQL94Dialect extends PostgreSQL94Dialect {
    public MyPostgreSQL94Dialect() {
        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
    }
}
