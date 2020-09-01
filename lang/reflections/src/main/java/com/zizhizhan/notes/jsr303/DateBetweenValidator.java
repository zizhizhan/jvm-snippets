package com.zizhizhan.notes.jsr303;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 11/27/13
 *         Time: 8:40 PM
 */
public class DateBetweenValidator implements ConstraintValidator<DateBetween, Date> {

    public final static String NULL = "null";
    public final static String NOW = "now";
    public final static String TODAY = "today";
    public final static String YESTERDAY = "yesterday";
    public final static String TOMORROW = "tomorrow";

    private Date startDate;
    private Date endDate;
    private DateFormat dateFormat;

    @Override
    public void initialize(DateBetween annotation) {
        this.dateFormat = new SimpleDateFormat(annotation.format());
        try {
            this.startDate = parseDate(annotation.startDate());
            this.endDate = parseDate(annotation.endDate());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        boolean result = true;
        if (startDate != null) {
            result = startDate.getTime() <= value.getTime();
        }
        if (endDate != null) {
            result = result && value.getTime() < endDate.getTime();
        }
        return result;
    }

    private Date parseDate(String dateString) throws ParseException {
        if (NULL.equalsIgnoreCase(dateString)) {
            return null;
        } else if (NOW.equalsIgnoreCase(dateString)) {
            return new Date();
        } else if (TODAY.equalsIgnoreCase(dateString)) {
            return dateWithoutTime(0);
        } else if (YESTERDAY.equalsIgnoreCase(dateString)) {
            return dateWithoutTime(-1);
        } else if (TOMORROW.equalsIgnoreCase(dateString)) {
            return dateWithoutTime(1);
        } else {
            return dateFormat.parse(dateString);
        }
    }

    private static Date dateWithoutTime(int from) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, from);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
