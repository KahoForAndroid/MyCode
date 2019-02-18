package zj.health.health_v1.Model;

import com.github.mikephil.charting.data.BaseEntry;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/25.
 */

public class Chart implements Serializable{

    private List<String> xStringType;//x轴字符串数据数组
    private List<? extends BaseEntry> entries;//图表数据
    private List<List<? extends BaseEntry>> entriesList;//图表数据(用于多条线)
    private int type;

    public List<List<? extends BaseEntry>> getEntriesList() {
        return entriesList;
    }

    public void setEntriesList(List<List<? extends BaseEntry>> entriesList) {
        this.entriesList = entriesList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getxStringType() {
        return xStringType;
    }

    public void setxStringType(List<String> xStringType) {
        this.xStringType = xStringType;
    }

    public List<? extends BaseEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<? extends BaseEntry> entries) {
        this.entries = entries;
    }
}
