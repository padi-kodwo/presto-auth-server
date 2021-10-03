package com.presto.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.presto.auth.enums.UserStatus;
import com.presto.auth.interfaces.TenantSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;


@Entity
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Table(name = "users", indexes = {
        @Index(name = "index_users_on_email", columnList = "email"),
        @Index(name = "index_users_on_phone", columnList = "phone"),
})
@Setter
@Getter
@ToString
public class User implements Serializable, TenantSupport {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Column(name = "phone", unique = true)
    private String phone;

    @NotNull
    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "other_names")
    private String otherNames;

    @Column(name = "onboard_status")
    private String onboardStatus;

    @Column(name = "user_status")
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;

    @Column(name = "successive_failed_attempts")
    private int successiveFailedAttempts;

    @NotNull
    @Column(name = "logged_in", nullable = false)
    private boolean loggedIn;

    @CreationTimestamp
    @Column(name = "last_seen")
    private Timestamp lastSeen;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    @CreationTimestamp
    @Column(name = "created")
    private Timestamp created;

    @UpdateTimestamp
    @Column(name = "updated")
    private Timestamp updated;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_code")})
    private Set<Role> roles;
}
