package zj.health.health_v1.Utils;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/8.
 */

public class DateUtil {



    /**
     * Kaho 2018.4.8
     * @param month Date对象
     * @return 返回yyyy年的字符串对象
     */
    public static String getYearStr(Date month){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年");
        return simpleDateFormat.format(month);
    }

    public static String getYearStr2(Date month){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(month);
    }
    /**
     * Kaho 2018.4.8
     * @param month Date对象
     * @return 返回yyyy年的字符串对象
     */
    public static String getDaysStr(Date month){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return simpleDateFormat.format(month);
    }

    /**
     * Kaho 2018.4.8
     * @param month Date对象
     * @return 返回HHmm（24小时制）的字符串对象
     */
    public static String getDaysStrhm(Date month){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(month);
    }


    /**
     * Kaho 2018.4.8
     */
    public static String getNowdayymd(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sDateFormat.format(new java.util.Date());
    }

    /**
     * Kaho 2018.4.8
     */
    public static String getNowdayym(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM");
        return sDateFormat.format(new java.util.Date());
    }
    /**
     * Kaho 2018.4.8
     * @param month Date对象
     * @return 返回yyyy年MM月的字符串对象
     */
    public static String getMonthStr(Date month){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月");
        return simpleDateFormat.format(month);
    }
    public static String getMonthStrYM(Date month){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        return simpleDateFormat.format(month);
    }
    /**
     * Kaho 2018.4.8
     * @param month Date对象
     * @return 返回yyyy年MM月的字符串对象
     */
    public static String getDateStr(Date month){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(month);
    }
    public static String getDateStrYmd(Date month){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(month);
    }
    public static String getDateStrYmdHm(Date month){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(month);
    }
    public static String getDateStrYmd2(Date month){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d");
        return simpleDateFormat.format(month);
    }

    /**
     * Kaho 2018.4.8
     * @param str 日期格式字符串
     * @return 返回指定月份的Date对象
     * */
    public static Date str2Date(String str){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月");
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Kaho 2018.4.8
     * @param str 日期格式字符串
     * @return 返回指定月份的Date对象
     * */
    public static Date str2Dates(String str){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }




    /**
     * Kaho 2018.4.8
     * @param str 日期格式字符串
     * @return 返回指定月份的Date对象
     * */
    public static Date str2Dates2(String str){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间转换为时间戳
     */
    public static Long dateToStamp_long(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();

        return ts;
    }

    /**
     * Kaho 2018.4.8
     * @param str 日期格式字符串
     * @return 返回指定月份的Date对象
     * */
    public static Date str2Dates3(String str){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前时间（年月日时分）
     * @return
     */
    public static String getNowDay(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return sDateFormat.format(new java.util.Date());
    }

    public static Date initDateByDay() {
           Calendar calendar = Calendar.getInstance();
           calendar.setTime(new Date());
           calendar.set(Calendar.HOUR, 0);
           calendar.set(Calendar.MINUTE, 0);
           calendar.set(Calendar.SECOND, 0);
           return calendar.getTime();
       }



    /**
     * 获取当前时间（时分）
     * @return
     */
    public static String getNowDayHM(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm");
        return sDateFormat.format(new java.util.Date());
    }


    public TimePickerView ShowTimePickerView(Context context,TimePickerView.OnTimeSelectListener listener,Calendar startDate,
                                             boolean year,boolean month, boolean day,boolean hour,
                                             boolean Minute,boolean second){

        Calendar enddate = Calendar.getInstance();
        TimePickerView timePickerView = new TimePickerView.Builder(context,listener).setType(new boolean[]
                {year, month, day, hour, Minute, second}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("年", "月", "日",
                        "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .setDividerColor(context.getResources().getColor(R.color.title_bottom_line))
                .setTextColorCenter(context.getResources().getColor(R.color.main_color))//设置选中项的颜色
                .setTextColorOut(context.getResources().getColor(R.color.background_gray3))//设置没有被选中项的颜色
                .setContentSize(21)
                .setRangDate(startDate,enddate)
                .setDate(enddate)
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(-10, 0,10, 0,
                        0, 0)//设置X轴倾斜角度[ -90 , 90°]
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
        return timePickerView;
    }

    /***
     * 判斷兩個日期是否是同一天
     * */
    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }


    /****
     * 判斷兩個日期是否是同一周
     * */
    public static boolean compare(Date firstDate, Date secondDate) throws ParseException {
        /** 以下先根据第一个时间找出所在周的星期一、星期日, 再对第二个时间进行比较 */
        SimpleDateFormat datetimeDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendarMonday = Calendar.getInstance();
        calendarMonday.setTime(firstDate);

        // 获取firstDate在当前周的第几天. （星期一~星期日：1~7）
        int monday = calendarMonday.get(Calendar.DAY_OF_WEEK);
        if (monday == 0) monday = 7;

        // 星期一开始时间
        calendarMonday.add(Calendar.DAY_OF_MONTH, -monday + 1);
        calendarMonday.set(Calendar.HOUR, 0);
        calendarMonday.set(Calendar.MINUTE, 0);
        calendarMonday.set(Calendar.SECOND, 0);

        // 星期日结束时间
        Calendar calendarSunday = Calendar.getInstance();
        calendarSunday.setTime(calendarMonday.getTime());
        calendarSunday.add(Calendar.DAY_OF_MONTH, 6);
        calendarSunday.set(Calendar.HOUR, 23);
        calendarSunday.set(Calendar.MINUTE, 59);
        calendarSunday.set(Calendar.SECOND, 59);

        System.out.println("星期一开始时间：" + datetimeDf.format(calendarMonday.getTime()));
        System.out.println("星期日结束时间：" + datetimeDf.format(calendarSunday.getTime()));

        // 比较第二个时间是否与第一个时间在同一周
        if (secondDate.getTime() >= calendarMonday.getTimeInMillis() &&
                secondDate.getTime() <= calendarSunday.getTimeInMillis()) {
            return true;
        }
        return false;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDistanceTime(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
    }

    //时间戳转String 格式：1990-01-01 12:00:00
    public static String setTimeToDate(long time){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(time);
        return d;
    }

    /**
     * 两个时间相差多少分钟
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long 相差分钟数
     */
    public static long getDistanceTimeMinute(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(day<1){
            return hour * 60 + min;
        }else{
            return day * 24 * hour * 60 + min;
        }
    }

    /***
     * 比較兩個時間的大小
     * */
    public static boolean compareDate(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);
            int index = d1.compareTo(d2);
            if (index > -1) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    /***
     * 比較兩個時間的大小
     * */
    public static boolean compareDateYmd(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);
            int index = d1.compareTo(d2);
            if (index > -1) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }


    public static String dayForWeek(String pTime) throws Exception {
        List<String> weekList =
                Arrays.asList(Health_AppLocation.instance.getResources().getStringArray(R.array.week_days));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return weekList.get(dayForWeek-1);
    }


    public static int weekForMonth(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        return c.get(Calendar.WEEK_OF_MONTH);
    }


    public static String dayForMonth(String pTime) throws Exception {
        List<String> monthList =
                Arrays.asList(Health_AppLocation.instance.getResources().getStringArray(R.array.month2));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        return monthList.get(c.get(Calendar.MONTH));
    }


    public static int dayForMonthtoInt(String pTime) throws Exception {
        List<String> monthList =
                Arrays.asList(Health_AppLocation.instance.getResources().getStringArray(R.array.month2));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        return c.get(Calendar.MONTH)+1;
    }

    public static String DateForYear(String time)throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(time));
        return c.get(Calendar.YEAR)+"";
    }

    /** 判断时间是否在时间段内     
     * * @param nowTime     
     * * @param beginTime     
     * * @param endTime     
     * * @return     
     * */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
         Calendar date = Calendar.getInstance();
         date.setTime(nowTime);
         //设置开始时间     
         Calendar begin = Calendar.getInstance();
          begin.setTime(beginTime);
         //设置结束时间       
         Calendar end = Calendar.getInstance();
         end.setTime(endTime);
         //处于开始时间之后，和结束时间之前的判断     
         if (date.after(begin) && date.before(end)) {
          return true;
         } else {return false;
         }
    }

    public static String UTCToCST(String UTCStr) throws ParseException {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+SSSS");
        date = sdf.parse(UTCStr);
//        System.out.println("UTC时间: " + date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
//        //calendar.getTime() 返回的是Date类型，也可以使用calendar.getTimeInMillis()获取时间戳
        return getDateStrYmdHm(calendar.getTime());

    }


    public static Long UTCToCST_long(String UTCStr) throws ParseException {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+SSSS");
        date = sdf.parse(UTCStr);
//        System.out.println("UTC时间: " + date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
//        //calendar.getTime() 返回的是Date类型，也可以使用calendar.getTimeInMillis()获取时间戳
        return calendar.getTimeInMillis();

    }



}
