package zj.health.health_v1.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/6/1.
 */

@Entity
public class ReportModel implements Parcelable {

    @Id(autoincrement = false)
    private String id;
    private String date;
    private String images;
    private String flag;
    private String addTime;
    private String logo;
    private String type;
    private String checkItems;
    private String checkItemId;
    private String title;
    private String userId;
    private String mark;

    @Generated(hash = 1773894656)
    public ReportModel(String id, String date, String images, String flag,
            String addTime, String logo, String type, String checkItems,
            String checkItemId, String title, String userId, String mark) {
        this.id = id;
        this.date = date;
        this.images = images;
        this.flag = flag;
        this.addTime = addTime;
        this.logo = logo;
        this.type = type;
        this.checkItems = checkItems;
        this.checkItemId = checkItemId;
        this.title = title;
        this.userId = userId;
        this.mark = mark;
    }

    @Generated(hash = 748802013)
    public ReportModel() {
    }

    protected ReportModel(Parcel in) {
        id = in.readString();
        date = in.readString();
        images = in.readString();
        flag = in.readString();
        addTime = in.readString();
        logo = in.readString();
        type = in.readString();
        checkItems = in.readString();
        checkItemId = in.readString();
        title = in.readString();
        userId = in.readString();
        mark = in.readString();
    }

    public static final Creator<ReportModel> CREATOR = new Creator<ReportModel>() {
        @Override
        public ReportModel createFromParcel(Parcel in) {
            return new ReportModel(in);
        }

        @Override
        public ReportModel[] newArray(int size) {
            return new ReportModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(String checkItems) {
        this.checkItems = checkItems;
    }

    public String getCheckItemId() {
        return checkItemId;
    }

    public void setCheckItemId(String checkItemId) {
        this.checkItemId = checkItemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(date);
        parcel.writeString(images);
        parcel.writeString(flag);
        parcel.writeString(addTime);
        parcel.writeString(logo);
        parcel.writeString(type);
        parcel.writeString(checkItems);
        parcel.writeString(checkItemId);
        parcel.writeString(title);
        parcel.writeString(userId);
        parcel.writeString(mark);
    }
}
