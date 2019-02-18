package zj.health.health_v1.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 生活模式实体类
 * Created by Administrator on 2018/6/1.
 */

@Entity
public class LiveMode {

    @Id(autoincrement = false)
    private String id;
    private String desc;

    @Generated(hash = 552372275)
    public LiveMode(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    @Generated(hash = 834591933)
    public LiveMode() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
