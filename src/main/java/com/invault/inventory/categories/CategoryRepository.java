package com.invault.inventory.categories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository responsible for database operations related to Category entities.
 *
 * Spring Data JPA automatically creates the implementation of this interface
 * at runtime, using the method names defined below.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Finds a category by its exact name, ignoring uppercase/lowercase differences.
    Optional<Category> findByNameIgnoreCase(String name);

    // Checks if a category name already exists before creating or updating it.
    boolean existsByNameIgnoreCase(String name);

    // Returns all active categories ordered alphabetically by name.
    List<Category> findByActiveTrueOrderByNameAsc();

    // Returns categories filtered by active/inactive state.
    List<Category> findByActiveOrderByNameAsc(boolean active);

    // Searches categories by partial name, useful later for filters or autocomplete.
    List<Category> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
}

/*
 * Explanation:
 * This repository centralizes all database queries related to Category.
 * It will be used later by services to list active categories, validate duplicate names,
 * search categories by text, and assign categories to products.
 */