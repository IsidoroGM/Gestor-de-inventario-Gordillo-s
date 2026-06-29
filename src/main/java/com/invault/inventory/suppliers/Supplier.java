package com.invault.inventory.suppliers;

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
 * Entity that represents a supplier.
 *
 * Suppliers can be optionally linked to inbound stock movements.
 * This helps track where products or materials come from.
 */
@Entity
@Table(name = "suppliers")
public class Supplier {

    // Primary key generated automatically by the database.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Supplier business name.
     *
     * Examples:
     * - Industrial Supplies S.L.
     * - Packaging Solutions
     * - Metal Parts Europe
     */
    @Column(nullable = false, unique = true, length = 150)
    private String name;

    // Optional contact person inside the supplier company.
    @Column(name = "contact_name", length = 120)
    private String contactName;

    // Optional supplier phone number.
    @Column(length = 30)
    private String phone;

    // Optional supplier email address.
    @Column(length = 120)
    private String email;

    // Optional notes about the supplier.
    @Column(length = 255)
    private String notes;

    // Allows disabling a supplier without deleting it physically from the database.
    @Column(nullable = false)
    private Boolean active = true;

    // Timestamp automatically assigned when the supplier is first created.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Timestamp automatically updated whenever the supplier changes.
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Supplier() {
        // Empty constructor required by JPA.
    }

    public Supplier(String name, String contactName, String phone, String email, String notes) {
        this.name = normalizeName(name);
        this.contactName = contactName;
        this.phone = phone;
        this.email = normalizeEmail(email);
        this.notes = notes;
        this.active = true;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();

        // Defensive default in case the entity is created without active value.
        if (this.active == null) {
            this.active = true;
        }

        // Keeps supplier data consistent before inserting into the database.
        this.name = normalizeName(this.name);
        this.email = normalizeEmail(this.email);
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();

        // Keeps supplier data consistent before updating the database.
        this.name = normalizeName(this.name);
        this.email = normalizeEmail(this.email);
    }

    /*
     * Normalizes the supplier name by removing unnecessary spaces.
     *
     * Example:
     * "  Industrial Supplies S.L.  " becomes "Industrial Supplies S.L."
     */
    private String normalizeName(String name) {
        return name == null ? null : name.trim();
    }

    /*
     * Normalizes the supplier email to avoid duplicated formats.
     *
     * Example:
     * " CONTACT@SUPPLIER.COM " becomes "contact@supplier.com"
     */
    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = normalizeEmail(email);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
 * The Supplier entity represents an external company or provider.
 * In the MVP, suppliers are optional and will mainly be used to identify the
 * origin of inbound stock movements without making the inventory flow too complex.
 */