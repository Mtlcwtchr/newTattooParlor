package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.config.GlobalRegEx;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    private DateUtils() {

    }

    public static LocalDate toLocal(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime toLocalDT(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date dateTimeFromHtmlString(String htmlString) {
        return toDate(LocalDateTime.parse(htmlString, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    public static Date dateFromHtmlString(String htmlString) {
        return toDate(LocalDate.parse(htmlString, DateTimeFormatter.ISO_LOCAL_DATE));
    }

    public static String dateToHtmlString(Date date) {
        return toLocal(date).format(DateTimeFormatter.ofPattern(GlobalRegEx.HTML_DATE_FORMAT));
    }

    public static String dateTimeToHtmlString(Date date) {
        return toLocalDT(date).format(DateTimeFormatter.ofPattern(GlobalRegEx.HTML_DATETIME_FORMAT));
    }
}
