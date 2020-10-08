package net.mavroprovato.springcms.repository;

import net.mavroprovato.springcms.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * The category repository
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    /**
     * Returns all categories ordered by name.
     *
     * @return All categories ordered by name.
     */
    List<Category> findAllByOrderByNameAsc();

    /**
     * Find a tag by slug.
     *
     * @param slug The slug.
     * @return The tag.
     */
    Optional<Category> findBySlug(String slug);
}
