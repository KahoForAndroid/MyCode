package zj.health.health_v1.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/10/8.
 */

public class RemindCalendarModel implements Serializable{

    private String id;
    private String medicineName;
    private String takeTime;
    private String dosage;
    private String dosageUnit;
    private List<String> reminderTime;
    private String remark;
    private List<String> eventTimes;
    private List<ReminderDaily> reminderDaily;

    public List<ReminderDaily> getReminderDaily() {
        return reminderDaily;
    }

    public void setReminderDaily(List<ReminderDaily> reminderDaily) {
        this.reminderDaily = reminderDaily;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
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

    public List<String> getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(List<String> reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getEventTimes() {
        return eventTimes;
    }

    public void setEventTimes(List<String> eventTimes) {
        this.eventTimes = eventTimes;
    }

    public static class ReminderDaily{
        String eventTime;
        List<events> events;

        public String getEventTime() {
            return eventTime;
        }

        public void setEventTime(String eventTime) {
            this.eventTime = eventTime;
        }

        public List<ReminderDaily.events> getEvents() {
            return events;
        }

        public void setEvents(List<ReminderDaily.events> events) {
            this.events = events;
        }

        public static class events{
           public String reminderTime;
           public boolean take;

            public String getReminderTime() {
                return reminderTime;
            }

            public void setReminderTime(String reminderTime) {
                this.reminderTime = reminderTime;
            }

            public boolean isTake() {
                return take;
            }

            public void setTake(boolean take) {
                this.take = take;
            }
        }
    }
}
