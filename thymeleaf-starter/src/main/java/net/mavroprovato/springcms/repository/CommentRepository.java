package net.mavroprovato.springcms.repository;

import net.mavroprovato.springcms.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The comment repository
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(
            "SELECT c FROM Comment c WHERE c.post.status = 'PUBLISHED'"
    )
    Page<Comment> findPublished(Pageable pageable);
}
