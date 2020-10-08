package net.mavroprovato.springcms.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.search.annotations.Field;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * Object mapping for content items.
 */
@Entity
@Table(indexes = {
        @Index(columnList = "type, status, publishedAt")
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "type")
abstract class Content {

    /** The unique identifier of the content */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    /** The content item title */
    @Column(nullable = false)
    @Field
    @Getter @Setter
    private String title;

    /** The content */
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    @Field
    @Getter @Setter
    private String content;

    /** The content status */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter @Setter
    private ContentStatus status = ContentStatus.DRAFT;

    /** The content item slug */
    @Column(unique = true)
    @Getter @Setter
    private String slug;

    /** The content item creation date */
    @Column(nullable = false)
    @CreationTimestamp
    @Getter
    private OffsetDateTime createdAt;

    /** The content item update date */
    @Column(nullable = false)
    @UpdateTimestamp
    @Getter
    private OffsetDateTime updatedAt;

    /** The content item publication date */
    @Column
    @Getter @Setter
    private OffsetDateTime publishedAt;

    /** The author of the content item */
    @ManyToOne(optional = false)
    @Getter @Setter
    private User author;
}
