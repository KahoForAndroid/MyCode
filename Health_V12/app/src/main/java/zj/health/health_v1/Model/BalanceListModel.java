package zj.health.health_v1.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/16.
 */

public class BalanceListModel implements Serializable{

    private int type;
    private String desc;
    private String cost;
    private String createdDate;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
