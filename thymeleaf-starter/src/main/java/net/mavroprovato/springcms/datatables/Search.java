package net.mavroprovato.springcms.datatables;

import lombok.Data;

/**
 * A search predicate, to be applied on a field or globally.
 */
@Data
final class Search {

    /** The search value. To be applied to all columns which have searchable as true. */
    private String value;

    /** True if the search filter should be treated as a regular expression for advanced searching, false otherwise. */
    private boolean regex;
}
