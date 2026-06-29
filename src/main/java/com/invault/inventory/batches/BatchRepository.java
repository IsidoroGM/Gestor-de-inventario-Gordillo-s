package com.invault.inventory.batches;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invault.inventory.products.Product;

/**
 * Repository responsible for database operations related to Batch entities.
 *
 * A batch represents a specific stock group of a product.
 * Products can have multiple batches, and available stock will be calculated
 * using the quantities stored in available batches.
 */
@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

    // Finds a batch by its unique manual batch code, ignoring uppercase/lowercase differences.
    Optional<Batch> findByBatchCodeIgnoreCase(String batchCode);

    // Checks if a batch code already exists before creating a new batch.
    boolean existsByBatchCodeIgnoreCase(String batchCode);

    // Returns all batches belonging to a specific product, ordered by creation date.
    List<Batch> findByProductOrderByCreatedAtAsc(Product product);

    // Returns all batches belonging to a product and filtered by batch status.
    List<Batch> findByProductAndStatusOrderByCreatedAtAsc(Product product, BatchStatus status);

    // Returns all batches with a specific status.
    List<Batch> findByStatusOrderByCreatedAtAsc(BatchStatus status);

    // Returns available batches for a product with quantity greater than the given value.
    // Later, this will help StockService find batches that can be used for outbound movements.
    List<Batch> findByProductAndStatusAndQuantityGreaterThanOrderByCreatedAtAsc(
            Product product,
            BatchStatus status,
            BigDecimal quantity
    );

    // Finds the oldest available batch for a product with quantity greater than the given value.
    // This supports the rule: outbound stock should suggest the oldest available batch first.
    Optional<Batch> findFirstByProductAndStatusAndQuantityGreaterThanOrderByCreatedAtAsc(
            Product product,
            BatchStatus status,
            BigDecimal quantity
    );

    // Searches batches by partial batch code, useful later for filters or autocomplete.
    List<Batch> findByBatchCodeContainingIgnoreCaseOrderByCreatedAtAsc(String batchCode);
}

/*
 * Explanation:
 * This repository centralizes all database queries related to Batch.
 * It will be used later by BatchService and StockService to validate unique batch codes,
 * list product batches, find available batches, and suggest the oldest usable batch
 * when creating outbound stock movements.
 */