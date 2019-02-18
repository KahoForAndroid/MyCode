package zj.health.health_v1.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2018/8/29.
 */
@Entity
public class ConsultTimeModel {

    @Id(autoincrement = false)
    private long id;
    private String consultId;
    private String consultTime;

    @Generated(hash = 1148833964)
    public ConsultTimeModel(long id, String consultId, String consultTime) {
        this.id = id;
        this.consultId = consultId;
        this.consultTime = consultTime;
    }

    @Generated(hash = 1498477566)
    public ConsultTimeModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConsultId() {
        return consultId;
    }

    public void setConsultId(String consultId) {
        this.consultId = consultId;
    }

    public String getConsultTime() {
        return consultTime;
    }

    public void setConsultTime(String consultTime) {
        this.consultTime = consultTime;
    }


}
