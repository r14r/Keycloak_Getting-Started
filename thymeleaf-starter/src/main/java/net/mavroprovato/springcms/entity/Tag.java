package net.mavroprovato.springcms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Tags for content
 */
@Entity
public class Tag {

    /** The unique identifier of the tag */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Integer id;

    /** The tag name */
    @Column(nullable = false)
    @Getter @Setter
    @NotNull(message = "{admin.tag.name.notNull}")
    private String name;

    /** The tag slug */
    @Column(unique = true)
    @Getter @Setter
    @NotNull(message = "{admin.tag.slug.notNull}")
    private String slug;

    /** The tag description */
    @Column()
    @Getter @Setter
    private String description;
}
