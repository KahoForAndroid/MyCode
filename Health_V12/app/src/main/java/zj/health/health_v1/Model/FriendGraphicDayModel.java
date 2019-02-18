package zj.health.health_v1.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/11/9.
 */

public class FriendGraphicDayModel implements Serializable{

    private List<GraphicDay> temp;
    private List<GraphicDay> heart_rate;
    private List<GraphicDay> weight;
    private List<GraphicDay> blood_glucose;
    private List<GraphicDay> blood_pressure;
    private String Title;//图片标题
    private String lable;//描述标签
    private String average;//平均值
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<GraphicDay> getTemp() {
        return temp;
    }

    public void setTemp(List<GraphicDay> temp) {
        this.temp = temp;
    }

    public List<GraphicDay> getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(List<GraphicDay> heart_rate) {
        this.heart_rate = heart_rate;
    }

    public List<GraphicDay> getWeight() {
        return weight;
    }

    public void setWeight(List<GraphicDay> weight) {
        this.weight = weight;
    }

    public List<GraphicDay> getBlood_glucose() {
        return blood_glucose;
    }

    public void setBlood_glucose(List<GraphicDay> blood_glucose) {
        this.blood_glucose = blood_glucose;
    }

    public List<GraphicDay> getBlood_pressure() {
        return blood_pressure;
    }

    public void setBlood_pressure(List<GraphicDay> blood_pressure) {
        this.blood_pressure = blood_pressure;
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

    public static class GraphicDay{

        public String timestamp;
        public String diastolicPressure;
        public String systolicPressure;
        public String heartRate;
        public String val;
        public String Title;//图片标题
        public String lable;//描述标签
        public String average;//平均值
        public int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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
