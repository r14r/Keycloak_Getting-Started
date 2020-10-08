package net.mavroprovato.springcms.repository;

import net.mavroprovato.springcms.dto.CountByMonth;
import net.mavroprovato.springcms.entity.Category;
import net.mavroprovato.springcms.entity.ContentStatus;
import net.mavroprovato.springcms.entity.Post;
import net.mavroprovato.springcms.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The content repository.
 */
public interface PostRepository extends JpaRepository<Post, Integer> {

    /**
     * Return the number of posts that where published per month.
     *
     * @return The number of posts that where published per month.
     */
    @Query(
        "SELECT new net.mavroprovato.springcms.dto.CountByMonth(YEAR(p.publishedAt), MONTH(p.publishedAt), COUNT(p)) " +
        "FROM Post p " +
        "GROUP BY YEAR(p.publishedAt), MONTH(p.publishedAt) " +
        "ORDER BY YEAR(p.publishedAt) DESC, MONTH(p.publishedAt) DESC"
    )
    List<CountByMonth> countByMonth();

    /**
     * Return a page of posts by status.
     *
     * @param status The content item status.
     * @param pageable The pagination parameters.
     * @return The content item page.
     */
    Page<Post> findByStatus(ContentStatus status, Pageable pageable);

    /**
     * Return a page of posts published between two dates.
     *
     * @param startDateTime The start dates.
     * @param endDateTime The end dates.
     * @param pageable The pagination parameters.
     * @return The post page.
     */
    Page<Post> findByStatusAndPublishedAtBetween(ContentStatus status, OffsetDateTime startDateTime,
                                                 OffsetDateTime endDateTime, Pageable pageable);

    /**
     * Find posts by status and tag.
     *
     * @param tag The tag.
     * @param pageable The pagination parameters.
     * @return The post page.
     */
    Page<Post> findByStatusAndTags(ContentStatus status, Tag tag, Pageable pageable);

    /**
     * Find post by status and category.
     *
     * @param category The category.
     * @param pageable The pagination parameters.
     * @return The post page.
     */
    Page<Post> findByStatusAndCategories(ContentStatus status, Category category, Pageable pageable);

    /**
     * Get a post by slug.
     *
     * @param slug The slug.
     * @return The post.
     */
    Optional<Post> findOneBySlug(String slug);
}
