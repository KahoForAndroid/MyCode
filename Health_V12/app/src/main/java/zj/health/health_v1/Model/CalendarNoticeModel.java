package zj.health.health_v1.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/14.
 */

public class CalendarNoticeModel implements Serializable{

    private String notificationId;
    private String id;
    private String type;
    private String isShowBtn;
    private List<infoAry> infoAry;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsShowBtn() {
        return isShowBtn;
    }

    public void setIsShowBtn(String isShowBtn) {
        this.isShowBtn = isShowBtn;
    }
    public List<CalendarNoticeModel.infoAry> getInfoAry() {
        return infoAry;
    }

    public void setInfoAry(List<CalendarNoticeModel.infoAry> infoAry) {
        this.infoAry = infoAry;
    }


    public static class infoAry implements Serializable{
         String lTitle;
         String rText;
         String repeat;
         String editType;
         String url;
         List<String> itemAry;
         boolean isShow;
         List<radioAry> radioAry;
         List<List<String>> pickerAry;




        public List<List<String>> getPickerAry() {
            return pickerAry;
        }

        public void setPickerAry(List<List<String>> pickerAry) {
            this.pickerAry = pickerAry;
        }


        public String getRepeat() {
            return repeat;
        }

        public void setRepeat(String repeat) {
            this.repeat = repeat;
        }


        public String getlTitle() {
            return lTitle;
        }

        public void setlTitle(String lTitle) {
            this.lTitle = lTitle;
        }

        public String getrText() {
            return rText;
        }

        public void setrText(String rText) {
            this.rText = rText;
        }

        public String getEditType() {
            return editType;
        }

        public void setEditType(String editType) {
            this.editType = editType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<String> getItemAry() {
            return itemAry;
        }

        public void setItemAry(List<String> itemAry) {
            this.itemAry = itemAry;
        }

        public boolean getIsShow() {
            return isShow;
        }

        public void setIsShow(boolean isShow) {
            this.isShow = isShow;
        }
        public List<CalendarNoticeModel.infoAry.radioAry> getRadioAry() {
            return radioAry;
        }

        public void setRadioAry(List<CalendarNoticeModel.infoAry.radioAry> radioAry) {
            this.radioAry = radioAry;
        }


        public static class radioAry implements Serializable {
            boolean checked;
            String value;


            public boolean isChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

        }

    }
}
