package com.invault.inventory.stock;

import com.invault.inventory.batches.Batch;
import com.invault.inventory.products.Product;
import com.invault.inventory.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository responsible for database operations related to StockMovement entities.
 *
 * A stock movement represents a traceable inventory operation:
 * inbound, outbound, positive adjustment or negative adjustment.
 */
@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    // Returns all stock movements of a specific product, ordered from newest to oldest.
    List<StockMovement> findByProductOrderByMovementDateDesc(Product product);

    // Returns all stock movements of a specific batch, ordered from newest to oldest.
    List<StockMovement> findByBatchOrderByMovementDateDesc(Batch batch);

    // Returns all stock movements created by a specific user, ordered from newest to oldest.
    List<StockMovement> findByUserOrderByMovementDateDesc(User user);

    // Returns all stock movements of a specific movement type.
    List<StockMovement> findByMovementTypeOrderByMovementDateDesc(MovementType movementType);

    // Returns stock movements between two dates.
    List<StockMovement> findByMovementDateBetweenOrderByMovementDateDesc(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Returns stock movements of a product between two dates.
    List<StockMovement> findByProductAndMovementDateBetweenOrderByMovementDateDesc(
            Product product,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Returns stock movements of a product filtered by movement type.
    List<StockMovement> findByProductAndMovementTypeOrderByMovementDateDesc(
            Product product,
            MovementType movementType
    );

    // Returns stock movements of a user between two dates.
    List<StockMovement> findByUserAndMovementDateBetweenOrderByMovementDateDesc(
            User user,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Returns the latest stock movements in the system.
    List<StockMovement> findTop20ByOrderByMovementDateDesc();
}

/*
 * Explanation:
 * This repository centralizes all database queries related to StockMovement.
 * It will be used later by StockService to review stock history, filter movements
 * by product, batch, user, type or date range, and show the latest inventory activity
 * in dashboards or product detail screens.
 */