package zj.health.health_v1.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/6/20.
 */

@Entity
public class DrugNameModel {

    @Id(autoincrement = true)
    private long id;
    private String DrugName;
    @Generated(hash = 1213034307)
    public DrugNameModel(long id, String DrugName) {
        this.id = id;
        this.DrugName = DrugName;
    }
    @Generated(hash = 629894004)
    public DrugNameModel() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getDrugName() {
        return this.DrugName;
    }
    public void setDrugName(String DrugName) {
        this.DrugName = DrugName;
    }
}
