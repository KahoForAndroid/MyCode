package zj.health.health_v1.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/6/7.
 */

public class UserDevices implements Serializable{

    private String isShowAllGraphic;
    private String rangeStart;
    private String flag;
    private List<Devices> devices;
    private String id;
    private String title;
    private List<Targets> targets;
    private String desc;
    private String rangeEnd;
    private List<Tags> tags;

    public String getIsShowAllGraphic() {
        return isShowAllGraphic;
    }

    public void setIsShowAllGraphic(String isShowAllGraphic) {
        this.isShowAllGraphic = isShowAllGraphic;
    }

    public String getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(String rangeStart) {
        this.rangeStart = rangeStart;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<Devices> getDevices() {
        return devices;
    }

    public void setDevices(List<Devices> devices) {
        this.devices = devices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Targets> getTargets() {
        return targets;
    }

    public void setTargets(List<Targets> targets) {
        this.targets = targets;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(String rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }




    public static class Devices implements Serializable{
        String flag;
        String nickname;
        String logo;
        String id;
        String userDeviceId;
        String bluetoothUUID;
        String deviceName;
        List<Steps> steps;
        String NAME;

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserDeviceId() {
            return userDeviceId;
        }

        public void setUserDeviceId(String userDeviceId) {
            this.userDeviceId = userDeviceId;
        }

        public String getBluetoothUUID() {
            return bluetoothUUID;
        }

        public void setBluetoothUUID(String bluetoothUUID) {
            this.bluetoothUUID = bluetoothUUID;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public List<Steps> getSteps() {
            return steps;
        }

        public void setSteps(List<Steps> steps) {
            this.steps = steps;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }



        public static class Steps implements Serializable{
            String supportDeviceId;
            String parttern;
            String service;
            String property;
            String hex;
            String id;
            String bluetoothUUID;
            String device;
            String characteristic;
            String platform;

            public String getSupportDeviceId() {
                return supportDeviceId;
            }

            public void setSupportDeviceId(String supportDeviceId) {
                this.supportDeviceId = supportDeviceId;
            }

            public String getParttern() {
                return parttern;
            }

            public void setParttern(String parttern) {
                this.parttern = parttern;
            }

            public String getService() {
                return service;
            }

            public void setService(String service) {
                this.service = service;
            }

            public String getProperty() {
                return property;
            }

            public void setProperty(String property) {
                this.property = property;
            }

            public String getHex() {
                return hex;
            }

            public void setHex(String hex) {
                this.hex = hex;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getBluetoothUUID() {
                return bluetoothUUID;
            }

            public void setBluetoothUUID(String bluetoothUUID) {
                this.bluetoothUUID = bluetoothUUID;
            }

            public String getDevice() {
                return device;
            }

            public void setDevice(String device) {
                this.device = device;
            }

            public String getCharacteristic() {
                return characteristic;
            }

            public void setCharacteristic(String characteristic) {
                this.characteristic = characteristic;
            }

            public String getPlatform() {
                return platform;
            }

            public void setPlatform(String platform) {
                this.platform = platform;
            }
        }
    }

    public static class Targets implements Serializable{
        String deviceType;
        String targetId;

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }
    }


    public static class Tags implements Serializable{
        String subTitle;
        String tagId;
        String title;
        String deviceId;

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }
}
