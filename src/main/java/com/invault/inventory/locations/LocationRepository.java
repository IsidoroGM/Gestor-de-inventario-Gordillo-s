package com.invault.inventory.locations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository responsible for database operations related to Location entities.
 *
 * Spring Data JPA automatically creates the implementation of this interface
 * at runtime, using the method names defined below.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // Finds a location by its exact name, ignoring uppercase/lowercase differences.
    Optional<Location> findByNameIgnoreCase(String name);

    // Checks if a location name already exists before creating or updating it.
    boolean existsByNameIgnoreCase(String name);

    // Returns all active locations ordered alphabetically by name.
    List<Location> findByActiveTrueOrderByNameAsc();

    // Returns locations filtered by active/inactive state.
    List<Location> findByActiveOrderByNameAsc(boolean active);

    // Searches locations by partial name, useful later for filters or autocomplete.
    List<Location> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
}

/*
 * Explanation:
 * This repository centralizes all database queries related to Location.
 * It will be used later by services to list active locations, validate duplicate names,
 * search locations by text, and assign a main location to products.
 */