package com.invault.inventory.roles;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Entity that represents an application role.
 *
 * Roles define what actions a user can perform inside InVault.
 */
@Entity
@Table(name = "roles")
public class Role {

    // Primary key generated automatically by the database.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Official technical role name. It is stored as text in the database.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private RoleName name;

    // Human-readable explanation of what this role is used for.
    @Column(length = 255)
    private String description;

    // Allows disabling a role without deleting it physically from the database.
    @Column(nullable = false)
    private Boolean active = true;

    // Timestamp automatically assigned when the role is first created.
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Timestamp automatically updated whenever the role changes.
    private LocalDateTime updatedAt;

    public Role() {
        // Empty constructor required by JPA.
    }

    public Role(RoleName name, String description) {
        this.name = name;
        this.description = description;
        this.active = true;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

/*
 * The Role entity represents the permissions profile assigned to users.
 * In future steps, User will have a relationship with Role so each user can
 * have one or more roles such as ADMIN, SUPERVISOR, WAREHOUSE or READ_ONLY.
 */