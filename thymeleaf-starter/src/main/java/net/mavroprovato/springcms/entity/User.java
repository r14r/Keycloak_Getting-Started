package net.mavroprovato.springcms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Application users
 */
@Entity(name = "app_user")
public class User {

    /** The unique identifier of the user */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    /** The user name */
    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String userName;

    /** The user password */
    @Column(nullable = false)
    @JsonIgnore
    @Getter @Setter
    private String password;

    /** The user email */
    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String email;

    /** The user name */
    @Column
    @Getter @Setter
    private String name;

    /** The user web site */
    @Column
    @Getter @Setter
    private String webSite;

    /** The user creation date */
    @Column(nullable = false)
    @CreationTimestamp
    @Getter
    private OffsetDateTime createdAt;

    /** The user update date */
    @Column(nullable = false)
    @UpdateTimestamp
    @Getter
    private OffsetDateTime updatedAt;

    /** The user role */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter @Setter
    private Role role;
}
