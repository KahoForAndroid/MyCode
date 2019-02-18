package zj.health.health_v1.Model;

import java.io.Serializable;
import java.util.List;

/**
 * 发布健康咨询bean
 * Created by Administrator on 2018/8/21.
 */

public class ConsultModel implements Serializable {


    private String level;
    private String office;
    private String communicateType;
    private String region;
    private String desc;
    private float cost;
    private String createdTime;
    private String commType;
    private List<String> images;
    private String id;
    private int status;
    private String doctorName;
    private String doctorIcon;
    private String conversationId;
    private int ConversationListPosition;
    private String doctorId;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public int getConversationListPosition() {
        return ConversationListPosition;
    }

    public void setConversationListPosition(int conversationListPosition) {
        ConversationListPosition = conversationListPosition;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getDoctorIcon() {
        return doctorIcon;
    }

    public void setDoctorIcon(String doctorIcon) {
        this.doctorIcon = doctorIcon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getCommunicateType() {
        return communicateType;
    }

    public void setCommunicateType(String communicateType) {
        this.communicateType = communicateType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }


    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public List<String> getImages() {
        return images;
    }

    public String getCommType() {
        return commType;
    }

    public void setCommType(String commType) {
        this.commType = commType;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}