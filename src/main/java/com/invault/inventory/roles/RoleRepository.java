package com.invault.inventory.roles;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing Role data from the database.
 *
 * Spring Data JPA automatically creates the implementation at runtime.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /*
     * Finds a role by its official enum name.
     *
     * Example:
     * RoleName.ADMIN
     */
    Optional<Role> findByName(RoleName name);

    /*
     * Checks if a role already exists with the given enum name.
     *
     * This will be useful when creating default system roles.
     */
    boolean existsByName(RoleName name);

    /*
     * Returns all active roles.
     *
     * This avoids showing disabled roles in future user-management screens.
     */
    List<Role> findByActiveTrue();

    /*
     * Finds a role by name only if it is active.
     *
     * This is useful when assigning roles to users.
     */
    Optional<Role> findByNameAndActiveTrue(RoleName name);
}

/*
 * RoleRepository gives the application access to the roles table.
 * It provides basic CRUD operations through JpaRepository and also includes
 * custom query methods based on role name and active status.
 */