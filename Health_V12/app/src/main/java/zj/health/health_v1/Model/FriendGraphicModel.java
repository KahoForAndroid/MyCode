package zj.health.health_v1.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/10/25.
 */

public class FriendGraphicModel implements Serializable{

    private TypeData temp;
    private TypeData heart_rate;
    private TypeData weight;
    private TypeData blood_glucose;
    private TypeData blood_pressure;
    private String Title;
    private int Type;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public TypeData getTemp() {
        return temp;
    }

    public void setTemp(TypeData temp) {
        this.temp = temp;
    }

    public TypeData getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(TypeData heart_rate) {
        this.heart_rate = heart_rate;
    }

    public TypeData getWeight() {
        return weight;
    }

    public void setWeight(TypeData weight) {
        this.weight = weight;
    }

    public TypeData getBlood_glucose() {
        return blood_glucose;
    }

    public void setBlood_glucose(TypeData blood_glucose) {
        this.blood_glucose = blood_glucose;
    }

    public TypeData getBlood_pressure() {
        return blood_pressure;
    }

    public void setBlood_pressure(TypeData blood_pressure) {
        this.blood_pressure = blood_pressure;
    }

    public static class TypeData {

        List<Data> year;
        List<Data> daily;
        List<Data> monthly;
        String Title;
        String lable;//描述标签
        int Type;
        String timestamp;
        String diastolicPressure;
        String systolicPressure;
        String heartRate;
        String val;

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

        public List<Data> getYear() {
            return year;
        }

        public void setYear(List<Data> year) {
            this.year = year;
        }

        public List<Data> getDaily() {
            return daily;
        }

        public void setDaily(List<Data> daily) {
            this.daily = daily;
        }

        public List<Data> getMonthly() {
            return monthly;
        }

        public void setMonthly(List<Data> monthly) {
            this.monthly = monthly;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public int getType() {
            return Type;
        }

        public void setType(int type) {
            Type = type;
        }

        public String getLable() {
            return lable;
        }

        public void setLable(String lable) {
            this.lable = lable;
        }

        public static class Data {
            String year;
            String average;
            String Title;
            String month;
            String day;
            String sysAverage;
            String diaAverage;
            int Type;

            public int getType() {
                return Type;
            }

            public void setType(int type) {
                Type = type;
            }

            public String getTitle() {
                return Title;
            }

            public void setTitle(String title) {
                Title = title;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

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
    }
}
