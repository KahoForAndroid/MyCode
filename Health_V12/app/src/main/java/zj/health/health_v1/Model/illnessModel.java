package zj.health.health_v1.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/6.
 */

public class illnessModel implements Serializable{

    private String id;
    private String desc;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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
