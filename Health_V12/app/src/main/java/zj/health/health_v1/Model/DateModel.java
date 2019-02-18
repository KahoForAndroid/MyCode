package zj.health.health_v1.Model;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/7/6.
 */

public class DateModel {

    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;

    public DateModel(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
    }
}
