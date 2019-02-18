package zj.health.health_v1.Model;

import java.io.Serializable;

/**
 * 已邀请健康咨询医生列表bean
 * Created by Administrator on 2018/8/24.
 */

public class CounselingListModel implements Serializable{

    private String nickname;
    private String iconUrl;
    private String level;
    private String hospital;
    private String evaluate;
    private String id;
    private String consultingNum;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsultingNum() {
        return consultingNum;
    }

    public void setConsultingNum(String consultingNum) {
        this.consultingNum = consultingNum;
    }
}
