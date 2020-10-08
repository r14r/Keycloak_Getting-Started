package net.mavroprovato.springcms.repository;

import net.mavroprovato.springcms.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The tag repository
 */
public interface TagRepository extends JpaRepository<Tag, Integer> {

    /**
     * Find a tag by slug.
     *
     * @param slug The slug.
     * @return The tag.
     */
    Optional<Tag> findBySlug(String slug);
}
