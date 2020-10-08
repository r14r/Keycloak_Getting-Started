package net.mavroprovato.springcms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

/**
 * Categories for content
 */
@Entity
public class Category {

    /** The unique identifier of the category */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    /** The category name */
    @Column(nullable = false)
    @Getter @Setter
    private String name;

    /** The category description */
    @Column()
    @Getter @Setter
    private String description;

    /** The category slug */
    @Column(unique = true)
    @Getter @Setter
    private String slug;

    /** The parent category */
    @ManyToOne
    @Getter @Setter
    private Category parent;
}
