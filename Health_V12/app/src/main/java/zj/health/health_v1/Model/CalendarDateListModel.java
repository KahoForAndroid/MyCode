package zj.health.health_v1.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/6/13.
 */

@Entity
public class CalendarDateListModel {

    @Id(autoincrement = true)
    private long id;
    private String date;
    private String img;
    private String color;


    @Generated(hash = 491585666)
    public CalendarDateListModel(long id, String date, String img, String color) {
        this.id = id;
        this.date = date;
        this.img = img;
        this.color = color;
    }

    @Generated(hash = 1015329304)
    public CalendarDateListModel() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
