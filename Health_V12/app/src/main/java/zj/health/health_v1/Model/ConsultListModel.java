package zj.health.health_v1.Model;

import java.io.Serializable;
import java.util.List;

/**
 * 患者咨询列表bean
 * Created by Administrator on 2018/8/27.
 */

public class ConsultListModel implements Serializable{

    //医生查看患者咨询列表参数
    private String sex;
    private String age;
    private String nickname;
    private String iconUrl;
    private String createdTime;
    private String illnessDesc;
    private String consultId;
    private String userId;
    private String status;
    private String height;
    private String weight;
    private String liveMode;
    private String sportMode;
    private List<illnessModel> illness;
    private int conversationListPosition;
    private String conversationId;
    private List<String> images;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getConversationListPosition() {
        return conversationListPosition;
    }

    public void setConversationListPosition(int conversationListPosition) {
        this.conversationListPosition = conversationListPosition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<illnessModel> getIllness() {
        return illness;
    }

    public void setIllness(List<illnessModel> illness) {
        this.illness = illness;
    }

    public  String getSex() {
        return sex;
    }public void   setSex(String sex) {
        this.sex = sex;
    }public String getAge() {
        return age;
    }public void   setAge(String age) {
        this.age = age;
    }public String getNickname() {
        return nickname;
    }public void   setNickname(String nickname) {
        this.nickname = nickname;
    }public String getIconUrl() {
        return iconUrl;
    }public void   setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }public String getCreatedTime() {
        return createdTime;
    }public void   setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }public String getIllnessDesc() {
        return illnessDesc;
    }public void   setIllnessDesc(String illnessDesc) {
        this.illnessDesc = illnessDesc;
    }public String getConsultId() {
        return consultId;
    }public void   setConsultId(String consultId) {
        this.consultId = consultId;
    }public String getUserId() {
        return userId;
    }public void   setUserId(String userId) {
        this.userId = userId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

}
