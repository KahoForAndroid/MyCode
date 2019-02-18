package zj.health.health_v1.Model;

import java.io.Serializable;

/**
 * 通讯录类
 * Created by Administrator on 2018/5/4.
 */

public class Contacts implements Serializable{

    private String name;//姓名
    private String Phone;//电话号码
    private String isUser;//是否App用户
    private String firstPinYin;//首字母拼音
    private String PinYin;//姓名拼音

    public String getFirstPinYin() {
        return firstPinYin;
    }

    public void setFirstPinYin(String firstPinYin) {
        this.firstPinYin = firstPinYin;
    }

    public String getPinYin() {
        return PinYin;
    }

    public void setPinYin(String pinYin) {
        PinYin = pinYin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }
}
