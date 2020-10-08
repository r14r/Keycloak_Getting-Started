package net.mavroprovato.springcms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Object mapping for page.
 */
@Entity
@DiscriminatorValue(ContentType.Values.PAGE)
public class Page extends Content {

    /** The page order */
    @Column(name="page_order")
    @Getter @Setter
    private int order;
}
