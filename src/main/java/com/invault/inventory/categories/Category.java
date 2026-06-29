package com.invault.inventory.categories;

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
 * Entity that represents a product category.
 *
 * Categories help organize products into logical groups such as raw materials,
 * spare parts, packaging, tools or finished products.
 */
@Entity
@Table(name = "categories")
public class Category {

    // Primary key generated automatically by the database.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Category name shown in the application.
     *
     * Examples:
     * - Raw Material
     * - Spare Part
     * - Packaging
     * - Finished Product
     */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // Optional explanation about what kind of products belong to this category.
    @Column(length = 255)
    private String description;

    // Allows disabling a category without deleting it physically from the database.
    @Column(nullable = false)
    private Boolean active = true;

    // Timestamp automatically assigned when the category is first created.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Timestamp automatically updated whenever the category changes.
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Category() {
        // Empty constructor required by JPA.
    }

    public Category(String name, String description) {
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

        // Keeps category names consistent before inserting into the database.
        this.name = normalizeName(this.name);
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();

        // Keeps category names consistent before updating the database.
        this.name = normalizeName(this.name);
    }

    /*
     * Normalizes the category name to avoid duplicated formats.
     *
     * Example:
     * " raw material " becomes "Raw Material".
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
 * The Category entity represents a logical group of products.
 * It allows products to be organized by type and can be expanded in the future
 * with subcategories or hierarchical category structures if the application needs it.
 */