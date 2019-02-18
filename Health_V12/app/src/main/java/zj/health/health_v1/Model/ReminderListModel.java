package zj.health.health_v1.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/10/8.
 */

public class ReminderListModel implements Serializable{

    private String id;
    private String userId;
    private String medicineName;
    private String timesOneDay;
    private List<String> reminderTime;
    private String takeTime;
    private String dosage;
    private String dosageUnit;
    private String treatment;
    private String intervalMode;
    private String intervalTemplate;
    private String weekTemplate;
    private String remark;
    private String startTime;
    private String endTime;
    private String createdTime;
    private String updateTime;
    private String prescId;
//    private boolean isTimes;//用于标示是否在用户所选择的日期时间范围内

//    public boolean isTimes() {
//        return isTimes;
//    }
//
//    public void setTimes(boolean times) {
//        isTimes = times;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getTimesOneDay() {
        return timesOneDay;
    }

    public void setTimesOneDay(String timesOneDay) {
        this.timesOneDay = timesOneDay;
    }

    public List<String> getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(List<String> reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(String takeTime) {
        this.takeTime = takeTime;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDosageUnit() {
        return dosageUnit;
    }

    public void setDosageUnit(String dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getIntervalMode() {
        return intervalMode;
    }

    public void setIntervalMode(String intervalMode) {
        this.intervalMode = intervalMode;
    }

    public String getIntervalTemplate() {
        return intervalTemplate;
    }

    public void setIntervalTemplate(String intervalTemplate) {
        this.intervalTemplate = intervalTemplate;
    }

    public String getWeekTemplate() {
        return weekTemplate;
    }

    public void setWeekTemplate(String weekTemplate) {
        this.weekTemplate = weekTemplate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getPrescId() {
        return prescId;
    }

    public void setPrescId(String prescId) {
        this.prescId = prescId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
