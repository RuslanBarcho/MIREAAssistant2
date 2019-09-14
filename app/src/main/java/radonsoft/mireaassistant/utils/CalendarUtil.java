package radonsoft.mireaassistant.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarUtil {
    public static int getWeekType(){
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 0);
        int weekNumber = gc.get(Calendar.WEEK_OF_YEAR);
        Calendar calendar = Calendar.getInstance();
        int today = (calendar.get(Calendar.DAY_OF_WEEK));
        if (today == 1){
            weekNumber ++;
        }
        if (weekNumber % 2 == 0){
            return 1;
        } else {
            return 0;
        }
    }

    public static int getToday(){
        Calendar calendar = Calendar.getInstance();
        int today = (calendar.get(Calendar.DAY_OF_WEEK)) - 2;
        if (today == -1) today = 0;
        return today;
    }
}
