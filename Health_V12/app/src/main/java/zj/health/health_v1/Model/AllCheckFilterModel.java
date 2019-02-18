package zj.health.health_v1.Model;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 */

public class AllCheckFilterModel implements IPickerViewData{

    private String id;
    private String name;
    private String logo;
    private String parentId;
    private List<CheckFilter> items;


    public List<CheckFilter> getItem() {
        return items;
    }

    public void setItem(List<CheckFilter> item) {
        this.items = item;
    }

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String getPickerViewText() {
        return getName();
    }


    public static class CheckFilter implements IPickerViewData{

        private String id;
        private String name;
        private String logo;
        private String parentId;

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

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        @Override
        public String getPickerViewText() {
            return getName();
        }
    }
}
