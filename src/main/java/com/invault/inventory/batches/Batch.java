package com.invault.inventory.batches;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.invault.inventory.products.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Entity that represents a stock batch for a product.
 *
 * A product can have several batches, and each batch stores its own available quantity.
 */
@Entity
@Table(name = "batches")
public class Batch {

    // Primary key generated automatically by the database.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Product associated with this batch.
     *
     * Many batches can belong to the same product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /*
     * Manual batch code.
     *
     * This code is entered by the user and must be globally unique.
     * Example: LOT-2026-0001
     */
    @Column(name = "batch_code", nullable = false, unique = true, length = 100)
    private String batchCode;

    /*
     * Current quantity available in this batch.
     *
     * BigDecimal is used because inventory quantities may contain decimals.
     */
    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity = BigDecimal.ZERO;

    /*
     * Current status of the batch.
     *
     * Only AVAILABLE batches should be considered for normal stock availability.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BatchStatus status = BatchStatus.AVAILABLE;

    // Optional notes about the batch.
    @Column(length = 255)
    private String notes;

    // Timestamp automatically assigned when the batch is first created.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Timestamp automatically updated whenever the batch changes.
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Batch() {
        // Empty constructor required by JPA.
    }

    public Batch(Product product, String batchCode, BigDecimal quantity, String notes) {
        this.product = product;
        this.batchCode = normalizeBatchCode(batchCode);
        this.quantity = quantity != null ? quantity : BigDecimal.ZERO;
        this.status = BatchStatus.AVAILABLE;
        this.notes = notes;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();

        // Defensive default in case the entity is created without quantity.
        if (this.quantity == null) {
            this.quantity = BigDecimal.ZERO;
        }

        // Defensive default in case the entity is created without status.
        if (this.status == null) {
            this.status = BatchStatus.AVAILABLE;
        }

        // Keeps batch code consistent before inserting into the database.
        this.batchCode = normalizeBatchCode(this.batchCode);

        // Prevents negative stock at entity level.
        validateNonNegativeQuantity();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();

        // Defensive default in case quantity is accidentally set to null.
        if (this.quantity == null) {
            this.quantity = BigDecimal.ZERO;
        }

        // Defensive default in case status is accidentally set to null.
        if (this.status == null) {
            this.status = BatchStatus.AVAILABLE;
        }

        // Keeps batch code consistent before updating the database.
        this.batchCode = normalizeBatchCode(this.batchCode);

        // Prevents negative stock at entity level.
        validateNonNegativeQuantity();
    }

    /*
     * Normalizes the batch code to avoid duplicated formats.
     *
     * Example:
     * " lot-2026-0001 " becomes "LOT-2026-0001".
     */
    private String normalizeBatchCode(String batchCode) {
        return batchCode == null ? null : batchCode.trim().toUpperCase();
    }

    /*
     * Validates that the batch quantity is not negative.
     *
     * The main stock rules will live in the service layer, but this gives
     * the entity an additional protection against invalid data.
     */
    private void validateNonNegativeQuantity() {
        if (this.quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Batch quantity cannot be negative.");
        }
    }

    /*
     * Returns true if this batch can be considered available for stock operations.
     */
    public boolean isAvailable() {
        return BatchStatus.AVAILABLE.equals(this.status)
                && this.quantity != null
                && this.quantity.compareTo(BigDecimal.ZERO) > 0;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = normalizeBatchCode(batchCode);
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity != null ? quantity : BigDecimal.ZERO;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status != null ? status : BatchStatus.AVAILABLE;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

/*
 * The Batch entity represents a specific stock lot for a product.
 * It stores the batch code, current quantity and availability status.
 * Product stock will be calculated later by summing the quantities of available batches.
 */