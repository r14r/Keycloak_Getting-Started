package net.mavroprovato.springcms.datatables;

import lombok.Data;

/**
 * The ordering for a column.
 */
@Data
class Order {
    /** Enumeration for the orderings. */
    public enum Direction {
        /** Sort ascending. */
        asc,
        /** Sort descending. */
        desc
    }

    /** Column to which ordering should be applied. This is an index reference to the columns array of information that
     * is also submitted to the server. */
    private int column;

    /** Ordering direction for this column. It will be asc or desc to indicate ascending ordering or descending
     * ordering, respectively. */
    private Direction dir;
}
