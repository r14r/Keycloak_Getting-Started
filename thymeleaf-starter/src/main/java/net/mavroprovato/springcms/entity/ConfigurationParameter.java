package net.mavroprovato.springcms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Mapping for configuration parameters
 */
@Entity
public class ConfigurationParameter {

    /** The unique identifier of the content */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    /** The configuration parameter name */
    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String name;

    /** The configuration parameter value */
    @Column(nullable = false)
    @Getter @Setter
    private String value;
}
