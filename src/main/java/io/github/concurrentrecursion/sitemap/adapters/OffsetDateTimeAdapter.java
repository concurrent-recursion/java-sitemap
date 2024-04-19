package io.github.concurrentrecursion.sitemap.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * A custom XML adapter for converting OffsetDateTime objects to/from String format.
 */
public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime> {
    private static final DateTimeFormatter FORMAT_AUTO = new DateTimeFormatterBuilder()
            .appendPattern("yyyy[-MM[-dd]]['T'HH[:mm[:ss[.SSS]]][XXX]]")
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
            .toFormatter(Locale.ENGLISH);

    private static final DateTimeFormatter FORMAT_MILLISECONDS = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS[XXX]")
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
            .toFormatter(Locale.ENGLISH);

    private static final DateTimeFormatter FORMAT_SECONDS = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss[XXX]")
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
            .toFormatter(Locale.ENGLISH);

    private static final DateTimeFormatter FORMAT_MINUTES = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm[XXX]")
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
            .toFormatter(Locale.ENGLISH);

    private static final DateTimeFormatter FORMAT_DAYS = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
            .toFormatter(Locale.ENGLISH);


    @Override
    public OffsetDateTime unmarshal(String s) throws Exception {
        if(s == null){
            return null;
        }
        return OffsetDateTime.parse(s,FORMAT_AUTO);
    }

    @Override
    public String marshal(final OffsetDateTime offsetDateTime) throws Exception {
        if(offsetDateTime == null){return null;}
        if (offsetDateTime.get(ChronoField.MILLI_OF_SECOND) > 0) {
            return FORMAT_MILLISECONDS.format(offsetDateTime);
        } else if (offsetDateTime.getSecond() > 0) {
            return FORMAT_SECONDS.format(offsetDateTime);
        } else if ((offsetDateTime.getHour() + offsetDateTime.getMinute()) > 0) {
            return FORMAT_MINUTES.format(offsetDateTime);
        } else {
            return FORMAT_DAYS.format(offsetDateTime);
        }
    }
}
