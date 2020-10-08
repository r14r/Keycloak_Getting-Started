package net.mavroprovato.springcms.dto;

import lombok.Value;

import java.time.YearMonth;

/**
 * An object that holds counts of objects per month.
 */
@Value
public final class CountByMonth {

    /**
     * Create the object.
     *
     * @param year The year.
     * @param month The month of year, from 1 (January) to 12 (December)
     * @param count The object count.
     */
    public CountByMonth(int year, int month, long count) {
        this.month = YearMonth.of(year, month);
        this.count = count;
    }


    /** The month. **/
    private final YearMonth month;

    /** The object count. */
    private final long count;
}
