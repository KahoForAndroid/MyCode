package zj.health.health_v1.Model;

/**
 * 用于展示人体指定特征类型的实体类
 * Created by Administrator on 2018/6/1.
 */

public class DeviceType {

    private int deviceType;
    private String title;

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
