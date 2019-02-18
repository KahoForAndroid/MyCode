package zj.health.health_v1.Model;


import java.io.Serializable;
import java.util.List;

/**
 * 图表相关数据实体类
 * Created by Administrator on 2018/6/1.
 */

public class GraphicModel implements Serializable {

    private List<types> types;
    private List<graphics> graphics;

    public List<types> getTypes() {
        return types;
    }

    public void setTypes(List<types> types) {
        this.types = types;
    }

    public List<graphics> getGraphics() {
        return graphics;
    }

    public void setGraphics(List<graphics> graphics) {
        this.graphics = graphics;
    }






    public static class types implements Serializable{
        String deviceType;
        String title;

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


    public static class graphics implements Serializable{
        String graphicGap;
        String unit;
        String graphicEndRange;
        String graphicFlag;
        String showAllGraphic;
        String graphicType;
        String graphicCenter;
        String title;
        List<list> list;
        String graphicStartRange;
        String endDate;
        String afterStep;
        String beforeStep;

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getAfterStep() {
            return afterStep;
        }

        public void setAfterStep(String afterStep) {
            this.afterStep = afterStep;
        }

        public String getBeforeStep() {
            return beforeStep;
        }

        public void setBeforeStep(String beforeStep) {
            this.beforeStep = beforeStep;
        }

        public String getGraphicStartRange() {
            return graphicStartRange;
        }

        public void setGraphicStartRange(String graphicStartRange) {
            this.graphicStartRange = graphicStartRange;
        }

        public List<list> getList() {
            return list;
        }

        public void setList(List<list> list) {
            this.list = list;
        }

        public String getGraphicGap() {
            return graphicGap;
        }

        public void setGraphicGap(String graphicGap) {
            this.graphicGap = graphicGap;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getGraphicEndRange() {
            return graphicEndRange;
        }

        public void setGraphicEndRange(String graphicEndRange) {
            this.graphicEndRange = graphicEndRange;
        }

        public String getGraphicFlag() {
            return graphicFlag;
        }

        public void setGraphicFlag(String graphicFlag) {
            this.graphicFlag = graphicFlag;
        }

        public String getShowAllGraphic() {
            return showAllGraphic;
        }

        public void setShowAllGraphic(String showAllGraphic) {
            this.showAllGraphic = showAllGraphic;
        }

        public String getGraphicType() {
            return graphicType;
        }

        public void setGraphicType(String graphicType) {
            this.graphicType = graphicType;
        }

        public String getGraphicCenter() {
            return graphicCenter;
        }

        public void setGraphicCenter(String graphicCenter) {
            this.graphicCenter = graphicCenter;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public static class list implements Serializable{
            String minPoint;
            String maxPoint;
            List<data> data;
            String graphicFlag;
            String drawType;
            String title;
            String desc;

            public String getGraphicFlag() {
                return graphicFlag;
            }

            public void setGraphicFlag(String graphicFlag) {
                this.graphicFlag = graphicFlag;
            }

            public String getDrawType() {
                return drawType;
            }

            public void setDrawType(String drawType) {
                this.drawType = drawType;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getMinPoint() {
                return minPoint;
            }

            public void setMinPoint(String minPoint) {
                this.minPoint = minPoint;
            }

            public String getMaxPoint() {
                return maxPoint;
            }

            public void setMaxPoint(String maxPoint) {
                this.maxPoint = maxPoint;
            }

            public List<data> getData() {
                return data;
            }

            public void setData(List<data> data) {
                this.data = data;
            }


            public static class data implements Serializable{
                String date;
                List<String> yPos;
                String flag;
                String xTitle;
                String invisible;
                String xPos;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public List<String> getyPos() {
                    return yPos;
                }

                public void setyPos(List<String> yPos) {
                    this.yPos = yPos;
                }

                public String getFlag() {
                    return flag;
                }

                public void setFlag(String flag) {
                    this.flag = flag;
                }

                public String getxTitle() {
                    return xTitle;
                }

                public void setxTitle(String xTitle) {
                    this.xTitle = xTitle;
                }

                public String getInvisible() {
                    return invisible;
                }

                public void setInvisible(String invisible) {
                    this.invisible = invisible;
                }

                public String getxPos() {
                    return xPos;
                }

                public void setxPos(String xPos) {
                    this.xPos = xPos;
                }
            }
        }
    }
}
