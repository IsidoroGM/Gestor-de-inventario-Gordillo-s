package com.invault.inventory.suppliers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository responsible for database operations related to Supplier entities.
 *
 * Spring Data JPA automatically creates the implementation of this interface
 * at runtime, using the method names defined below.
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    // Finds a supplier by its exact name, ignoring uppercase/lowercase differences.
    Optional<Supplier> findByNameIgnoreCase(String name);

    // Checks if a supplier name already exists before creating or updating it.
    boolean existsByNameIgnoreCase(String name);

    // Finds a supplier by email, ignoring uppercase/lowercase differences.
    Optional<Supplier> findByEmailIgnoreCase(String email);

    // Checks if a supplier email already exists before creating or updating it.
    boolean existsByEmailIgnoreCase(String email);

    // Returns all active suppliers ordered alphabetically by name.
    List<Supplier> findByActiveTrueOrderByNameAsc();

    // Returns suppliers filtered by active/inactive state.
    List<Supplier> findByActiveOrderByNameAsc(boolean active);

    // Searches suppliers by partial name, useful later for filters or autocomplete.
    List<Supplier> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    // Searches suppliers by partial email, useful later for administration filters.
    List<Supplier> findByEmailContainingIgnoreCaseOrderByNameAsc(String email);
}

/*
 * Explanation:
 * This repository centralizes all database queries related to Supplier.
 * It will be used later by services to list active suppliers, validate duplicate names or emails,
 * search suppliers by text, and associate suppliers with inbound stock movements.
 */