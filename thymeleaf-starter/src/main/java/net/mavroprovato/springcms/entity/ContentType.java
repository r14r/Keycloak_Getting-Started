package net.mavroprovato.springcms.entity;

/**
 * Enumeration of the content types.
 */
public enum ContentType {
    /** Content type for posts */
    POST,
    /** Content type for pages */
    PAGE;

    /**
     * Constants for the JPA discriminator values.
     */
    public static class Values {
        static final String POST = "POST";
        static final String PAGE = "PAGE";
    }
}
