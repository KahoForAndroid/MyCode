package zj.health.health_v1.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/7/19.
 */
@Entity
public class Users{

    @Id(autoincrement = false)
    private Long _id;
    private String phone;
    private String nickname;
    private String birth;
    private String region;
    private String iconUrl;
    private boolean isDoctor;
    private String platform;
    private int sex;
    private int height;
    private int weight;
    private int liveMode;
    private int sportMode;
    private String referralCode;
    private String referrerCode;
    private String sig;

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getReferrerCode() {
        return referrerCode;
    }

    public void setReferrerCode(String referrerCode) {
        this.referrerCode = referrerCode;
    }



    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getLiveMode() {
        return liveMode;
    }

    public void setLiveMode(int liveMode) {
        this.liveMode = liveMode;
    }

    public int getSportMode() {
        return sportMode;
    }

    public void setSportMode(int sportMode) {
        this.sportMode = sportMode;
    }

    @Transient
    private securityAnswer securityAnswer;
    private String id;
    private String doctorStatus;
    private String token;
    private boolean registered;

    @Generated(hash = 2134115095)
    public Users(Long _id, String phone, String nickname, String birth,
            String region, String iconUrl, boolean isDoctor, String platform,
            int sex, int height, int weight, int liveMode, int sportMode,
            String referralCode, String referrerCode, String sig, String id,
            String doctorStatus, String token, boolean registered) {
        this._id = _id;
        this.phone = phone;
        this.nickname = nickname;
        this.birth = birth;
        this.region = region;
        this.iconUrl = iconUrl;
        this.isDoctor = isDoctor;
        this.platform = platform;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.liveMode = liveMode;
        this.sportMode = sportMode;
        this.referralCode = referralCode;
        this.referrerCode = referrerCode;
        this.sig = sig;
        this.id = id;
        this.doctorStatus = doctorStatus;
        this.token = token;
        this.registered = registered;
    }

    @Generated(hash = 2146996206)
    public Users() {
    }

    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorStatus() {
        return doctorStatus;
    }

    public void setDoctorStatus(String doctorStatus) {
        this.doctorStatus = doctorStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public Users.securityAnswer getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(Users.securityAnswer securityAnswer) {
        this.securityAnswer = securityAnswer;
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

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    public String getIconUrl() {
        return iconUrl;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public boolean getIsDoctor() {
        return isDoctor;
    }

    public void setIsDoctor(boolean isDoctor) {
        this.isDoctor = isDoctor;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public boolean getRegistered() {
        return this.registered;
    }


    public static class securityAnswer{
        String question1;
        String answer1;
        String question2;
        String answer2;

        public String getQuestion1() {
            return question1;
        }

        public void setQuestion1(String question1) {
            this.question1 = question1;
        }

        public String getAnswer1() {
            return answer1;
        }

        public void setAnswer1(String answer1) {
            this.answer1 = answer1;
        }

        public String getQuestion2() {
            return question2;
        }

        public void setQuestion2(String question2) {
            this.question2 = question2;
        }

        public String getAnswer2() {
            return answer2;
        }

        public void setAnswer2(String answer2) {
            this.answer2 = answer2;
        }
    }
}
