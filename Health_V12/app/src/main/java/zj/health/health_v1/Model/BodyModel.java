package zj.health.health_v1.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/27.
 */

public class BodyModel implements Serializable{

    private String id;
    private String name;
    private List<subItem> subItem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BodyModel.subItem> getSubItem() {
        return subItem;
    }

    public void setSubItem(List<BodyModel.subItem> subItem) {
        this.subItem = subItem;
    }

    public static class subItem implements Serializable{
        String sid;
        String name;
        String logo;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }
}
