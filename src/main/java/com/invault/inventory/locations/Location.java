package com.invault.inventory.locations;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Entity that represents a physical or logical inventory location.
 *
 * Locations help identify where products are mainly stored inside the company.
 * Examples: main warehouse, production area, receiving area or spare parts room.
 */
@Entity
@Table(name = "locations")
public class Location {

    // Primary key generated automatically by the database.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Location name shown in the application.
     *
     * Examples:
     * - Main Warehouse
     * - Production Area
     * - Spare Parts Room
     */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // Optional explanation about where this location is or how it should be used.
    @Column(length = 255)
    private String description;

    // Allows disabling a location without deleting it physically from the database.
    @Column(nullable = false)
    private Boolean active = true;

    // Timestamp automatically assigned when the location is first created.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Timestamp automatically updated whenever the location changes.
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Location() {
        // Empty constructor required by JPA.
    }

    public Location(String name, String description) {
        this.name = normalizeName(name);
        this.description = description;
        this.active = true;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();

        // Defensive default in case the entity is created without active value.
        if (this.active == null) {
            this.active = true;
        }

        // Keeps location names consistent before inserting into the database.
        this.name = normalizeName(this.name);
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();

        // Keeps location names consistent before updating the database.
        this.name = normalizeName(this.name);
    }

    /*
     * Normalizes the location name to avoid duplicated formats.
     *
     * Example:
     * " main warehouse " becomes "Main Warehouse".
     */
    private String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            return name;
        }

        String trimmedName = name.trim().toLowerCase();
        String[] words = trimmedName.split("\\s+");
        StringBuilder normalizedName = new StringBuilder();

        for (String word : words) {
            normalizedName
                    .append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        return normalizedName.toString().trim();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = normalizeName(name);
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
 * The Location entity represents the main place where products can be stored.
 * It is intentionally simple for the MVP and can be expanded later with fields
 * such as warehouse, aisle, shelf or zone if the inventory model needs more detail.
 */