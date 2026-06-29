package com.invault.inventory.products;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invault.inventory.categories.Category;
import com.invault.inventory.locations.Location;
import com.invault.inventory.units.Unit;

/**
 * Repository responsible for database operations related to Product entities.
 *
 * Spring Data JPA automatically creates the implementation of this interface
 * at runtime, using the method names defined below.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Finds a product by its unique internal SKU, ignoring uppercase/lowercase differences.
    Optional<Product> findBySkuIgnoreCase(String sku);

    // Checks if a SKU already exists before creating or updating a product.
    boolean existsBySkuIgnoreCase(String sku);

    // Searches products by partial name, useful later for inventory filters.
    List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    // Returns all active products ordered alphabetically by name.
    List<Product> findByActiveTrueOrderByNameAsc();

    // Returns products filtered by active/inactive state.
    List<Product> findByActiveOrderByNameAsc(boolean active);

    // Returns all products assigned to a specific category.
    List<Product> findByCategoryOrderByNameAsc(Category category);

    // Returns products assigned to a specific category and filtered by active/inactive state.
    List<Product> findByCategoryAndActiveOrderByNameAsc(Category category, boolean active);

    // Returns all products assigned to a specific location.
    List<Product> findByLocationOrderByNameAsc(Location location);

    // Returns products assigned to a specific location and filtered by active/inactive state.
    List<Product> findByLocationAndActiveOrderByNameAsc(Location location, boolean active);

    // Returns all products using a specific unit of measure.
    List<Product> findByUnitOrderByNameAsc(Unit unit);

    // Returns products using a specific unit and filtered by active/inactive state.
    List<Product> findByUnitAndActiveOrderByNameAsc(Unit unit, boolean active);

    // Searches active products by partial name.
    List<Product> findByNameContainingIgnoreCaseAndActiveTrueOrderByNameAsc(String name);

    // Searches active products by partial SKU.
    List<Product> findBySkuContainingIgnoreCaseAndActiveTrueOrderByNameAsc(String sku);
}

/*
 * Explanation:
 * This repository centralizes all database queries related to Product.
 * It will be used later by ProductService to create products, validate unique SKUs,
 * list active inventory items, search by name or SKU, and filter products by category,
 * location or unit of measure.
 */