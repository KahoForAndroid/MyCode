package zj.health.health_v1.Model;

import java.util.List;

/**
 * Created by Administrator on 2018/6/26.
 */

public class CalendarNoticeListModel {

    private String remark;
    private String time;
    private String title;
    private String type;
    private List<list> list;

    public List<CalendarNoticeListModel.list> getList() {
        return list;
    }

    public void setList(List<CalendarNoticeListModel.list> list) {
        this.list = list;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public static class list {
        public String item;
        public List<Datas> before;
        public List<Datas> after;
        public List<Datas> infos;

        public List<Datas> getBefore() {
            return before;
        }

        public void setBefore(List<Datas> before) {
            this.before = before;
        }

        public List<Datas> getAfter() {
            return after;
        }

        public void setAfter(List<Datas> after) {
            this.after = after;
        }

        public List<Datas> getInfos() {
            return infos;
        }

        public void setInfos(List<Datas> infos) {
            this.infos = infos;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }



        public static class Datas{
            String treatment;
            String flag;
            String addTime;
            String endDate;
            String mealType;
            String repeatType;
            String remark;
            String perTime;
            String itemName;
            String itemTimes;
            String cleanFlag;
            String repeat;
            String notificationId;
            String id;
            String perDay;
            String startDate;

            public String getTreatment() {
                return treatment;
            }

            public void setTreatment(String treatment) {
                this.treatment = treatment;
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

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public String getMealType() {
                return mealType;
            }

            public void setMealType(String mealType) {
                this.mealType = mealType;
            }

            public String getRepeatType() {
                return repeatType;
            }

            public void setRepeatType(String repeatType) {
                this.repeatType = repeatType;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getPerTime() {
                return perTime;
            }

            public void setPerTime(String perTime) {
                this.perTime = perTime;
            }

            public String getItemName() {
                return itemName;
            }

            public void setItemName(String itemName) {
                this.itemName = itemName;
            }

            public String getItemTimes() {
                return itemTimes;
            }

            public void setItemTimes(String itemTimes) {
                this.itemTimes = itemTimes;
            }

            public String getCleanFlag() {
                return cleanFlag;
            }

            public void setCleanFlag(String cleanFlag) {
                this.cleanFlag = cleanFlag;
            }

            public String getRepeat() {
                return repeat;
            }

            public void setRepeat(String repeat) {
                this.repeat = repeat;
            }

            public String getNotificationId() {
                return notificationId;
            }

            public void setNotificationId(String notificationId) {
                this.notificationId = notificationId;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPerDay() {
                return perDay;
            }

            public void setPerDay(String perDay) {
                this.perDay = perDay;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }
        }


    }
}
