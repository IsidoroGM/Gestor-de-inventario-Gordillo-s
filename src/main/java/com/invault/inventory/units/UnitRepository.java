package com.invault.inventory.units;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository responsible for database operations related to Unit entities.
 *
 * Spring Data JPA will automatically create the implementation at runtime
 * based on the method names defined in this interface.
 */
@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    // Finds a unit by its technical code, for example: "KG", "PCS", "L".
    Optional<Unit> findByCodeIgnoreCase(String code);

    // Checks if a unit code already exists before creating or updating a unit.
    boolean existsByCodeIgnoreCase(String code);

    // Finds a unit by its name, ignoring uppercase/lowercase differences.
    Optional<Unit> findByNameIgnoreCase(String name);

    // Checks if a unit name already exists.
    boolean existsByNameIgnoreCase(String name);

    // Returns all active units ordered alphabetically by name.
    List<Unit> findByActiveTrueOrderByNameAsc();

    // Returns units filtered by active/inactive state.
    List<Unit> findByActiveOrderByNameAsc(boolean active);

    // Searches units by partial name, useful later for filters or autocomplete.
    List<Unit> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
}

/*
 * Explanation:
 * This repository gives the application a clean way to query Unit records
 * without writing SQL manually. It will be used later by services when creating
 * products, validating units, listing active units, or preventing duplicate unit codes.
 */