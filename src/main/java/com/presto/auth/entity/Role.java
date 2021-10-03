package com.presto.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "roles", indexes = {
        @Index(name = "index_roles_on_code", columnList = "code"),
        @Index(name = "index_roles_on_name", columnList = "name")
})
@Setter
@Getter
public class Role implements Serializable {

    @Id
    private int code;

    @NotNull
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @NotNull
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @Override
    public String toString() {
        return "Role{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", users=" + users +
                '}';
    }
}
