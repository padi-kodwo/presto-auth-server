package com.presto.auth.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "account_validation_tokens", indexes = {
        @Index(name = "index_account_validation_tokens_on_token", columnList = "token")
})
@Setter
@Getter
public class AccountValidationToken {

    @Id
    @Column(name = "token")
    private String token;

    @NotNull
    @Column(name = "user_id")
    private String userId;

    @NotNull
    @Column(name = "expires_at")
    private long expiresAt;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}