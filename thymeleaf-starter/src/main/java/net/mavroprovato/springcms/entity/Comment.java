package net.mavroprovato.springcms.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;

/**
 * A comment on a content item
 */
@Entity
public class Comment {

    /** The unique identifier of the comment */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    /** The comment content */
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    @NotNull(message = "{comment.comment.notNull}")
    @Size(min = 10, message = "{comment.comment.size}")
    @Getter @Setter
    private String comment;

    /** The name of the user that posted the comment */
    @Column(nullable = false)
    @NotNull(message = "{comment.name.notNull}")
    @Size(min = 5, max = 255, message = "{comment.name.size}")
    @Getter @Setter
    private String name;

    /** The email of the user that posted the comment */
    @Column(nullable = false)
    @NotNull(message = "{comment.email.notNull}")
    @Size(min = 5, max = 255, message = "{comment.email.size}")
    @Email(message = "{comment.email.email}")
    @Getter @Setter
    private String email;

    /** The web site of the user that posted the comment */
    @Column
    @Size(min = 10, max = 255, message = "{comment.webSite.size}")
    @URL(message = "{comment.webSite.url}")
    @Getter @Setter
    private String webSite;

    /** The comment creation date */
    @Column(nullable = false)
    @CreationTimestamp
    @Getter
    private OffsetDateTime createdAt;

    /** The comment update date */
    @Column(nullable = false)
    @UpdateTimestamp
    @Getter
    private OffsetDateTime updatedAt;

    /** The post that this comment belong to */
    @ManyToOne
    @JoinColumn
    @Getter @Setter
    private Post post;
}
