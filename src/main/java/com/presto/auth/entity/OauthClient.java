package com.presto.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "oauth_client_details",
        indexes = { @Index(name = "index_oauth_client_details_on_client_id", columnList = "client_id", unique = true)
})
@Setter
@Getter
public class OauthClient {

    @Id
    @Column(name = "client_id")
    private String clientId;

    @NotNull
    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "resource_ids")
    private String resourceIds;

    @Column(name = "scope")
    private String scope;

    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    @Column(name = "web_server_redirect_uri")
    private String webServerRedirectUri;

    @Column(name = "authorities")
    private String authorities;

    @Column(name = "access_token_validity")
    private int accessTokenValidity;

    @Column(name = "refresh_token_validity")
    private int refreshTokenValidity;

    @Column(name = "additional_information", length = 4096)
    private String additionalInformation;

    @Column(name = "autoapprove")
    private String autoApprove;
}