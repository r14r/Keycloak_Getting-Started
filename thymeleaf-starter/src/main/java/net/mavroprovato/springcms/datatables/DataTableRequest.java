package net.mavroprovato.springcms.datatables;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the request that is sent by the data table javascript library.
 */
@Data
public final class DataTableRequest {

    /** Draw counter. This is used by DataTables to ensure that the Ajax returns from server-side processing requests
     * are drawn in sequence by DataTables. */
    private int draw;

    /** Paging first record indicator. This is the start point in the current data set. */
    private int start;

    /** Number of records that the table can display in the current draw. It is expected that the number of records
     * returned will be equal to this number, unless the server has fewer records to return. Note that this can be -1
     * to indicate that all records should be returned. */
    private int length;

    /** The global search. */
    private Search search;

    /** The data table column specification. */
    private List<Column> columns;

    /** The ordering list. */
    private List<Order> order;

    /**
     * Returns the page request for the data table request.
     *
     * @return The page request for the data table request.
     */
    public PageRequest getPageRequest() {
        return PageRequest.of(start / length, length, Sort.by(this.order.stream().map(o -> new Sort.Order(
                Sort.Direction.valueOf(o.getDir().toString().toUpperCase()),
                this.columns.get(o.getColumn()).getData()
        )).collect(Collectors.toList())));
    }
}
