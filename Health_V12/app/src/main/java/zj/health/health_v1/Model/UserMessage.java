package zj.health.health_v1.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/4.
 */

public class UserMessage implements Serializable{

    private String sex;
    private String height;
    private String weight;
    private String liveMode;
    private String sportMode;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLiveMode() {
        return liveMode;
    }

    public void setLiveMode(String liveMode) {
        this.liveMode = liveMode;
    }

    public String getSportMode() {
        return sportMode;
    }

    public void setSportMode(String sportMode) {
        this.sportMode = sportMode;
    }
}
