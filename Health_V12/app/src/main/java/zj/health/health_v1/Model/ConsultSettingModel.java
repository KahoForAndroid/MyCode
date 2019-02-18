package zj.health.health_v1.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/20.
 */

public class ConsultSettingModel implements Serializable{

    private String region;
    private boolean all;
    private String communicationType;
    private float chatPrice;
    private float videoPrice;
    private String introduction;
    private String speciality;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public String getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(String communicationType) {
        this.communicationType = communicationType;
    }

    public float getChatPrice() {
        return chatPrice;
    }

    public void setChatPrice(float chatPrice) {
        this.chatPrice = chatPrice;
    }

    public float getVideoPrice() {
        return videoPrice;
    }

    public void setVideoPrice(float videoPrice) {
        this.videoPrice = videoPrice;
    }
}
