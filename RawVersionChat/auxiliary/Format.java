package auxiliary;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Format {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("d.MM.YYYY");
    public static final SimpleDateFormat DAYS_FORMAT = new SimpleDateFormat("d");
    public static final SimpleDateFormat MONTHS_FORMAT = new SimpleDateFormat("MMMM");
    public static final SimpleDateFormat YEARS_FORMAT = new SimpleDateFormat("YYYY");

    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("H:mm:ss");
    public static final SimpleDateFormat HOURS_FORMAT = new SimpleDateFormat("H");
    public static final SimpleDateFormat MINUTES_FORMAT = new SimpleDateFormat("m");
    public static final SimpleDateFormat SECONDS_FORMAT = new SimpleDateFormat("s");

    public static String getFormattedInformation(SimpleDateFormat simpleDateFormat, Date date) {
        return simpleDateFormat.format(date);
    }
}