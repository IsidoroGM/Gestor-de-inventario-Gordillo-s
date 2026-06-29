package com.invault.inventory.stock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.invault.inventory.batches.Batch;
import com.invault.inventory.products.Product;
import com.invault.inventory.suppliers.Supplier;
import com.invault.inventory.users.User;

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
import jakarta.persistence.Table;

/**
 * Entity that represents a stock movement.
 *
 * Every stock change must create a movement to keep the inventory traceable.
 */
@Entity
@Table(name = "stock_movements")
public class StockMovement {

    // Primary key generated automatically by the database.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Product affected by this stock movement.
     *
     * This makes it easy to query the full movement history of a product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /*
     * Batch affected by this stock movement.
     *
     * Since InVault works with batches, every stock movement must be linked
     * to the specific batch whose quantity changed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    /*
     * User who performed the movement.
     *
     * This keeps traceability over who changed the inventory.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /*
     * Optional supplier.
     *
     * This will mainly be used for INBOUND movements to know where stock came from.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    /*
     * Type of stock movement.
     *
     * Example:
     * - INBOUND
     * - OUTBOUND
     * - POSITIVE_ADJUSTMENT
     * - NEGATIVE_ADJUSTMENT
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 40)
    private MovementType movementType;

    /*
     * Movement quantity.
     *
     * This value is always positive. The movement type determines if stock
     * increases or decreases.
     */
    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;

    /*
     * Batch quantity before applying the movement.
     */
    @Column(name = "previous_batch_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal previousBatchQuantity;

    /*
     * Batch quantity after applying the movement.
     */
    @Column(name = "new_batch_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal newBatchQuantity;

    /*
     * Optional reason explaining why the stock movement was made.
     *
     * Example:
     * - Initial stock
     * - Production output
     * - Inventory count correction
     */
    @Column(length = 255)
    private String reason;

    /*
     * Date and time when the movement was registered.
     */
    @Column(name = "movement_date", nullable = false, updatable = false)
    private LocalDateTime movementDate;

    public StockMovement() {
        // Empty constructor required by JPA.
    }

    public StockMovement(
            Product product,
            Batch batch,
            User user,
            Supplier supplier,
            MovementType movementType,
            BigDecimal quantity,
            BigDecimal previousBatchQuantity,
            BigDecimal newBatchQuantity,
            String reason
    ) {
        this.product = product;
        this.batch = batch;
        this.user = user;
        this.supplier = supplier;
        this.movementType = movementType;
        this.quantity = quantity;
        this.previousBatchQuantity = previousBatchQuantity;
        this.newBatchQuantity = newBatchQuantity;
        this.reason = normalizeReason(reason);
    }

    @PrePersist
    public void onCreate() {
        // Movement date is assigned only when the movement is first created.
        if (this.movementDate == null) {
            this.movementDate = LocalDateTime.now();
        }

        this.reason = normalizeReason(this.reason);

        validateMovementData();
    }

    /*
     * Normalizes the reason text by removing unnecessary spaces.
     *
     * Example:
     * "  Initial stock  " becomes "Initial stock".
     */
    private String normalizeReason(String reason) {
        return reason == null ? null : reason.trim();
    }

    /*
     * Validates the most important movement rules at entity level.
     *
     * The full business logic will live in StockService, but this adds an
     * extra safety layer before saving invalid movement data.
     */
    private void validateMovementData() {
        if (this.movementType == null) {
            throw new IllegalArgumentException("Movement type is required.");
        }

        if (this.quantity == null || this.quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Movement quantity must be greater than zero.");
        }

        if (this.previousBatchQuantity == null || this.previousBatchQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Previous batch quantity cannot be negative.");
        }

        if (this.newBatchQuantity == null || this.newBatchQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("New batch quantity cannot be negative.");
        }
    }

    /*
     * Returns true if this movement increases stock.
     */
    public boolean increasesStock() {
        return MovementType.INBOUND.equals(this.movementType)
                || MovementType.POSITIVE_ADJUSTMENT.equals(this.movementType);
    }

    /*
     * Returns true if this movement decreases stock.
     */
    public boolean decreasesStock() {
        return MovementType.OUTBOUND.equals(this.movementType)
                || MovementType.NEGATIVE_ADJUSTMENT.equals(this.movementType);
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Batch getBatch() {
        return batch;
    }

    public User getUser() {
        return user;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getPreviousBatchQuantity() {
        return previousBatchQuantity;
    }

    public BigDecimal getNewBatchQuantity() {
        return newBatchQuantity;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getMovementDate() {
        return movementDate;
    }
}

/*
 * The StockMovement entity represents a traceable inventory change.
 * It stores the affected product, batch, responsible user, optional supplier,
 * movement type, quantity, previous stock, new stock, reason and movement date.
 * This allows InVault to keep a reliable stock history instead of changing
 * quantities without leaving evidence.
 */