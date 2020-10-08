package net.mavroprovato.springcms.entity;

/**
 * An enumeration of the defined configuration parameters.
 */
public enum Parameter {
    /** The site title. */
    TITLE("Title", String.class),
    /** How the site subtitle. */
    SUBTITLE("Subtitle", String.class),
    /** How many posts to display in the list page. */
    POSTS_PER_PAGE(10, Integer.class),
    /** The site absolute URL */
    SITE_URL("", String.class);

    /** The default value of the parameter */
    private final Object defaultValue;

    /** The parameter type */
    private final Class<?> type;

    /**
     * Create the parameter.
     *
     * @param defaultValue The default parameter value.
     * @param type The parameter type.
     */
    Parameter(Object defaultValue, Class<?> type) {
        this.defaultValue = defaultValue;
        this.type = type;
    }

    /**
     * Get the parameter default value.
     *
     * @return The parameter default value.
     */
    public Object defaultValue() {
        return defaultValue;
    }

    /**
     * Get the parameter type.
     *
     * @return The parameter type.
     */
    public Class<?> type() {
        return type;
    }
}
