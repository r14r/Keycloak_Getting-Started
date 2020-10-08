package net.mavroprovato.springcms.datatables;

import lombok.Data;

/**
 * A Data table's column.
 */
@Data
class Column {

    /** The column's data source */
    private String data;

    /** The column's name */
    private String name;

    /** Flag to indicate if this column is searchable (true) or not (false). */
    private boolean searchable;

    /** Flag to indicate if this column is orderable (true) or not (false). */
    private boolean orderable;

    /** Search value to apply to this specific column. */
    private Search search;
}
