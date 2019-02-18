package zj.health.health_v1.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/5/31.
 */

@Entity
public class UserInfo{

    private String shareCode;
    private String country;
    private String addTime;
    private String city;
    private String openId;
    private String sonCount;
    private String remark;
    private String language;
    private String type;
    private String platform;
    private String password;
    private String liveMode;
    private String sportMode;
    private String loginTime;
    private String weibo;
    private String province;
    private String nickname;
    private String logo;

    @Id(autoincrement = false)
    private long id;

    private String grandeSonCount;
    private String height;
    private String qq;
    private String sex;
    private String mobile;
    private String wechat;
    private String birth;
    private String weight;
    private String relatives;
    private String parentId;
    private String appid;
    private String name;
    private String account;

    @Generated(hash = 496466293)
    public UserInfo(String shareCode, String country, String addTime, String city,
            String openId, String sonCount, String remark, String language,
            String type, String platform, String password, String liveMode,
            String sportMode, String loginTime, String weibo, String province,
            String nickname, String logo, long id, String grandeSonCount,
            String height, String qq, String sex, String mobile, String wechat,
            String birth, String weight, String relatives, String parentId,
            String appid, String name, String account) {
        this.shareCode = shareCode;
        this.country = country;
        this.addTime = addTime;
        this.city = city;
        this.openId = openId;
        this.sonCount = sonCount;
        this.remark = remark;
        this.language = language;
        this.type = type;
        this.platform = platform;
        this.password = password;
        this.liveMode = liveMode;
        this.sportMode = sportMode;
        this.loginTime = loginTime;
        this.weibo = weibo;
        this.province = province;
        this.nickname = nickname;
        this.logo = logo;
        this.id = id;
        this.grandeSonCount = grandeSonCount;
        this.height = height;
        this.qq = qq;
        this.sex = sex;
        this.mobile = mobile;
        this.wechat = wechat;
        this.birth = birth;
        this.weight = weight;
        this.relatives = relatives;
        this.parentId = parentId;
        this.appid = appid;
        this.name = name;
        this.account = account;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSonCount() {
        return sonCount;
    }

    public void setSonCount(String sonCount) {
        this.sonCount = sonCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrandeSonCount() {
        return grandeSonCount;
    }

    public void setGrandeSonCount(String grandeSonCount) {
        this.grandeSonCount = grandeSonCount;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRelatives() {
        return relatives;
    }

    public void setRelatives(String relatives) {
        this.relatives = relatives;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setId(long id) {
        this.id = id;
    }
}
