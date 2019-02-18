package zj.health.health_v1.Model;

import android.app.Service;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/3.
 */

public class FriendModel implements Serializable{

    private String id;
    private String userId;
    private String phone;
    private String nickname;
    private String iconUrl;
    private FriendPermission friendPermission;

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


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public FriendPermission getFriendPermission() {
        return friendPermission;
    }

    public void setFriendPermission(FriendPermission friendPermission) {
        this.friendPermission = friendPermission;
    }

    public static class FriendPermission implements Serializable{

        String userId;
        Permission permission;


        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Permission getPermission() {
            return permission;
        }

        public void setPermission(Permission permission) {
            this.permission = permission;
        }

        public static class Permission implements Serializable{

            boolean bloodPressure;
            boolean weight;
            boolean heartRate;
            boolean temp;
            boolean bloodGlucose;
            boolean medicineReminder;
            boolean healthReport;
            boolean particularReport;

            public boolean isMedicineReminder() {
                return medicineReminder;
            }

            public void setMedicineReminder(boolean medicineReminder) {
                this.medicineReminder = medicineReminder;
            }

            public boolean isHealthReport() {
                return healthReport;
            }

            public void setHealthReport(boolean healthReport) {
                this.healthReport = healthReport;
            }

            public boolean isParticularReport() {
                return particularReport;
            }

            public void setParticularReport(boolean particularReport) {
                this.particularReport = particularReport;
            }

            public boolean isBloodPressure() {
                return bloodPressure;
            }

            public void setBloodPressure(boolean bloodPressure) {
                this.bloodPressure = bloodPressure;
            }

            public boolean isWeight() {
                return weight;
            }

            public void setWeight(boolean weight) {
                this.weight = weight;
            }

            public boolean isHeartRate() {
                return heartRate;
            }

            public void setHeartRate(boolean heartRate) {
                this.heartRate = heartRate;
            }

            public boolean isTemp() {
                return temp;
            }

            public void setTemp(boolean temp) {
                this.temp = temp;
            }

            public boolean isBloodGlucose() {
                return bloodGlucose;
            }

            public void setBloodGlucose(boolean bloodGlucose) {
                this.bloodGlucose = bloodGlucose;
            }
        }
    }

}
