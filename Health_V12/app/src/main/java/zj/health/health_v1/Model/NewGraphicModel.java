package zj.health.health_v1.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/9/14.
 */

public class NewGraphicModel implements Serializable{

    private String Title;//图片标题
    private String lable;//描述标签
    private String average;//平均值
    private List<Year> year;//年数据
    private List<Daily> daily;//日数据
    private List<Monthly> monthly;//月数据
    private List<GraphicDay> graphicDayList;//当日数据

    public List<GraphicDay> getGraphicDayList() {
        return graphicDayList;
    }

    public void setGraphicDayList(List<GraphicDay> graphicDayList) {
        this.graphicDayList = graphicDayList;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }
    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }
    public List<Year> getYear() {
        return year;
    }

    public void setYear(List<Year> year) {
        this.year = year;
    }

    public List<Daily> getDaily() {
        return daily;
    }

    public void setDaily(List<Daily> daily) {
        this.daily = daily;
    }

    public List<Monthly> getMonthly() {
        return monthly;
    }

    public void setMonthly(List<Monthly> monthly) {
        this.monthly = monthly;
    }



    public static class Year{

        public String year;
        public String sysAverage;
        public String diaAverage;
        private String average;//平均值

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getSysAverage() {
            return sysAverage;
        }

        public void setSysAverage(String sysAverage) {
            this.sysAverage = sysAverage;
        }

        public String getDiaAverage() {
            return diaAverage;
        }

        public void setDiaAverage(String diaAverage) {
            this.diaAverage = diaAverage;
        }
    }

    public static class Daily{

        public String month;
        public String year;
        public String day;
        public String sysAverage;
        public String diaAverage;
        private String average;//平均值

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }


        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getSysAverage() {
            return sysAverage;
        }

        public void setSysAverage(String sysAverage) {
            this.sysAverage = sysAverage;
        }

        public String getDiaAverage() {
            return diaAverage;
        }

        public void setDiaAverage(String diaAverage) {
            this.diaAverage = diaAverage;
        }
    }

    public static class Monthly{

        public String month;
        public String year;
        public String sysAverage;
        public String diaAverage;
        private String average;//平均值

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }


        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getSysAverage() {
            return sysAverage;
        }

        public void setSysAverage(String sysAverage) {
            this.sysAverage = sysAverage;
        }

        public String getDiaAverage() {
            return diaAverage;
        }

        public void setDiaAverage(String diaAverage) {
            this.diaAverage = diaAverage;
        }
    }

    public static class GraphicDay{

        public String timestamp;
        public String diastolicPressure;
        public String systolicPressure;
        public String heartRate;
        public String val;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getDiastolicPressure() {
            return diastolicPressure;
        }

        public void setDiastolicPressure(String diastolicPressure) {
            this.diastolicPressure = diastolicPressure;
        }

        public String getSystolicPressure() {
            return systolicPressure;
        }

        public void setSystolicPressure(String systolicPressure) {
            this.systolicPressure = systolicPressure;
        }

        public String getHeartRate() {
            return heartRate;
        }

        public void setHeartRate(String heartRate) {
            this.heartRate = heartRate;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }
}
