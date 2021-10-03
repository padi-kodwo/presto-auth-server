package com.presto.auth.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@SuppressWarnings({"LombokDataInspection", "JpaDataSourceORMInspection"})
@Entity
@Table(name = "oauth_access_token", indexes = {
        @Index(name = "index_oauth_access_token_token_id", columnList = "token_id", unique = true)
})
@Data
public class OauthAccessToken implements Serializable {

    @Id
    @Column(name = "token_id")
    private String tokenId;

    @NotNull
    @Column(name = "token")
    private String token;

    @Column(name = "authentication_id")
    private String authenticationId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "authentication")
    private String authentication;

    @Column(name = "refresh_token")
    private String refreshToken;
}