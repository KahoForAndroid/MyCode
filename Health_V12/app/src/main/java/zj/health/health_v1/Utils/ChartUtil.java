package zj.health.health_v1.Utils;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BaseEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.MyMarkerView;

/**
 * Created by Administrator on 2018/4/2.
 */

public class ChartUtil {


    /**
     * 显示折线图
     *
     * @param context     上下文对象
     * @param lineChart   折线对象
     * @param StringListX 存放X轴字符串
     * @param xCount      x轴的标签数量
     * @param yCount      y轴的标签数量
     * @param yMin        y轴的最大值
     * @param yMax        y轴的最小值
     * @param dataSets   数据线对象组数，用于第一次调用方法时使用(可存放多组数据)
     */
    public void showLineChat(Context context, LineChart lineChart, final List<String> StringListX,
                             int xCount, int yCount, float yMin, float yMax,String LimitLineLable,String LimitLineLable2,float average,float average2,String unit,LineDataSet... dataSets ) {

        LineData data = new LineData(dataSets);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//x轴所在位置
//        xAxis.setDrawGridLines(true);
        xAxis.setLabelCount(xCount, false);//x轴显示的条目数量
        xAxis.enableGridDashedLine(12f, 5, 0);//x轴设置虚线，注释默认为实线
        xAxis.setGranularity(1f);//间隔差距
        xAxis.setLabelRotationAngle(300);// 标签倾斜
        xAxis.setValueFormatter(new IAxisValueFormatter() {//x轴字符串格式化
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value<=0.0){
                     return "";
                }else if(value>=StringListX.size()){
                    return "";
                }else{
                    return StringUtil.trimNull(StringListX.get((int) value));
                }

            }
        });




        YAxis yAxis = lineChart.getAxisLeft();//获取左侧Y轴对象
        yAxis.setLabelCount(yCount, false);//Y轴显示的条目数量
        yAxis.setAxisMinimum(yMin);
        yAxis.setAxisMaximum(yMax);
        yAxis.enableGridDashedLine(12f, 5, 0);
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value + "";
            }
        });


        yAxis.removeAllLimitLines();

        LimitLine limitLine1 = new LimitLine(average, LimitLineLable);
        limitLine1.setLineWidth(2f);
        limitLine1.enableDashedLine(10f, 10f, 0f);
        limitLine1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        limitLine1.setTextSize(10f);
//        limitLine1.setLineColor(R.color.btn_red);
//        limitLine1.setTypeface(tf);

        // reset all limit lines to avoid overlapping lines
         yAxis.addLimitLine(limitLine1);

         if(average2>0){
             LimitLine limitLine2 = new LimitLine(average, LimitLineLable2);
             limitLine2.setLineWidth(2f);
             limitLine2.enableDashedLine(10f, 10f, 0f);
             limitLine2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
             limitLine2.setTextSize(10f);
             yAxis.addLimitLine(limitLine2);
//        limitLine1.setLineColor(R.color.btn_red);
//        limitLine1.setTypeface(tf);
         }
//        yAxis.addLimitLine(ll2);
//        rightYAxis.setAxisMinimum(0f);
        //rightYAxis.setLabelCount(11,true)
        lineChart.getAxisRight().setEnabled(false);//隐藏右侧Y轴

        MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);//由于点击坐标点显示的布局
        mv.setChartView(lineChart); // For bounds control
        lineChart.setMarker(mv); // Set the marker to the chart;
        lineChart.getDescription().setEnabled(false);
        lineChart.setDragEnabled(true);
//        lineChart.animateX(1000);
        // 是否可以缩放
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);


        Description description = new Description();  // 这部分是深度定制描述文本，颜色，字体等
        description.setText(unit);
        description.setTextSize(13);
        description.setTextColor(Color.RED);
        lineChart.setDescription(description);
        lineChart.setNoDataText("暂无数据");
//        lineChart.setBackground(context.getResources().getDrawable(R.drawable.line_bg));
//        lineChart.setTouchEnabled(true); // 所有触摸事件,默认true
//        lineChart.setDragEnabled(true);    // 可拖动,默认true
//        lineChart.setScaleEnabled(true);   // 两个轴上的缩放,X,Y分别默认为true
        lineChart.setScaleXEnabled(true);  // X轴上的缩放,默认true
        lineChart.setScaleYEnabled(false);  // Y轴上的缩放,默认true
//        lineChart.setPinchZoom(true);  // X,Y轴同时缩放，false则X,Y轴单独缩放,默认false
        lineChart.setDoubleTapToZoomEnabled(false); // 双击缩放,默认true
//        lineChart.setDragDecelerationEnabled(true);    // 抬起手指，继续滑动,默认true
//        lineChart.setDragDecelerationFrictionCoef(0.9f);   // 摩擦系数,[0-1]，较大值速度会缓慢下降，0，立即停止;1,无效值，并转换为0.9999.默认0.9f.
        lineChart.setData(data);
    }




    /**
     * 显示折线图
     *
     * @param context     上下文对象
     * @param lineChart   折线对象
     * @param StringListX 存放X轴字符串
     * @param xCount      x轴的标签数量
     * @param yCount      y轴的标签数量
     * @param yMin        y轴的最大值
     * @param yMax        y轴的最小值
     * @param dataSets   数据线对象组数，用于第一次调用方法时使用(可存放多组数据)
     */
    public void showLineChatForBlood_pressure(Context context, LineChart lineChart, final List<String> StringListX,
                             int xCount, int yCount, float yMin, float yMax,String unit,String LimitLineLable,String LimitLineLable2,float average,float average2,LineDataSet... dataSets ) {

        LineData data = new LineData(dataSets);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//x轴所在位置
        xAxis.setLabelCount(xCount, false);//x轴显示的条目数量
        xAxis.enableGridDashedLine(12f, 5, 0);//x轴设置虚线，注释默认为实线
        xAxis.setGranularity(1f);//间隔差距
        xAxis.setLabelRotationAngle(300);// 标签倾斜
        xAxis.setValueFormatter(new IAxisValueFormatter() {//x轴字符串格式化
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value<0){
                    return "";
                }else if(value>=StringListX.size()){
                    return "";
                }else{
                    return StringUtil.trimNull(StringListX.get((int) value));
                }

            }
        });

        YAxis yAxis = lineChart.getAxisLeft();//获取左侧Y轴对象
        yAxis.setLabelCount(yCount, false);//Y轴显示的条目数量
        yAxis.setAxisMinimum(yMin);
        yAxis.setAxisMaximum(yMax);
        yAxis.enableGridDashedLine(12f, 5, 0);
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value + "";
            }
        });
        yAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines


        LimitLine limitLine1 = new LimitLine(average, LimitLineLable);
        limitLine1.setLineWidth(2f);
        limitLine1.enableDashedLine(10f, 10f, 0f);
        limitLine1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        limitLine1.setTextSize(10f);
        yAxis.addLimitLine(limitLine1);

            LimitLine limitLine2 = new LimitLine(average2, LimitLineLable2);
            limitLine2.setLineWidth(2f);
            limitLine2.enableDashedLine(10f, 10f, 0f);
            limitLine2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            limitLine2.setTextSize(10f);
            yAxis.addLimitLine(limitLine2);

//        yAxis.addLimitLine(ll1);
//        yAxis.addLimitLine(ll2);
//        rightYAxis.setAxisMinimum(0f);
        //rightYAxis.setLabelCount(11,true)
        lineChart.getAxisRight().setEnabled(false);//隐藏右侧Y轴

        MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);//由于点击坐标点显示的布局
        mv.setChartView(lineChart); // For bounds control
        lineChart.setMarker(mv); // Set the marker to the chart;
        lineChart.getDescription().setEnabled(false);
        lineChart.setDragEnabled(true);
        // 是否可以缩放
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);


        Description description = new Description();  // 这部分是深度定制描述文本，颜色，字体等
        description.setText(unit);
        description.setTextSize(13);
        description.setTextColor(Color.RED);
        lineChart.setDescription(description);
        lineChart.setNoDataText("暂无数据");
//        lineChart.setBackground(context.getResources().getDrawable(R.drawable.line_bg));
//        lineChart.setTouchEnabled(true); // 所有触摸事件,默认true
//        lineChart.setDragEnabled(true);    // 可拖动,默认true
//        lineChart.setScaleEnabled(true);   // 两个轴上的缩放,X,Y分别默认为true
        lineChart.setScaleXEnabled(true);  // X轴上的缩放,默认true
        lineChart.setScaleYEnabled(false);  // Y轴上的缩放,默认true
//        lineChart.setPinchZoom(true);  // X,Y轴同时缩放，false则X,Y轴单独缩放,默认false
        lineChart.setDoubleTapToZoomEnabled(false); // 双击缩放,默认true
//        lineChart.setDragDecelerationEnabled(true);    // 抬起手指，继续滑动,默认true
//        lineChart.setDragDecelerationFrictionCoef(0.9f);   // 摩擦系数,[0-1]，较大值速度会缓慢下降，0，立即停止;1,无效值，并转换为0.9999.默认0.9f.
        lineChart.setData(data);
    }






    /**
     * 显示折线图
     *
     * @param context     上下文对象
     * @param lineChart   折线对象
     * @param StringListX 存放X轴字符串
     * @param xCount      x轴的标签数量
     * @param yCount      y轴的标签数量
     * @param yMin        y轴的最大值
     * @param yMax        y轴的最小值
     * @param dataSets   数据线对象组数，用于第一次调用方法时使用(可存放多组数据)
     */
    public void showLineChats(Context context, LineChart lineChart, final List<String> StringListX,
                             int xCount, int yCount, float yMin, float yMax,int bg,String LimitLineLable,String LimitLineLable2,float average,float average2,String unit,LineDataSet... dataSets ) {

        LineData data = new LineData(dataSets);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//x轴所在位置
        xAxis.setLabelCount(xCount, false);//x轴显示的条目数量
        xAxis.enableGridDashedLine(12f, 5, 0);//x轴设置虚线，注释默认为实线
        xAxis.setGranularity(1f);//间隔差距
        xAxis.setLabelRotationAngle(300);// 标签倾斜
        xAxis.setValueFormatter(new IAxisValueFormatter() {//x轴字符串格式化
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value<0){
                    return "";
                }else if(value>=StringListX.size()){
                    return "";
                }else{
                    return StringUtil.trimNull(StringListX.get((int) value));
                }

            }
        });

        YAxis yAxis = lineChart.getAxisLeft();//获取左侧Y轴对象
        yAxis.setLabelCount(yCount, false);//Y轴显示的条目数量
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(yMax);
        yAxis.enableGridDashedLine(12f, 5, 0);
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value + "";
            }
        });


        yAxis.removeAllLimitLines();

        LimitLine limitLine1 = new LimitLine(average, LimitLineLable);
        limitLine1.setLineWidth(2f);
        limitLine1.enableDashedLine(10f, 10f, 0f);
        limitLine1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        limitLine1.setTextSize(10f);

        yAxis.addLimitLine(limitLine1);

//        yAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        yAxis.addLimitLine(ll1);
//        yAxis.addLimitLine(ll2);
//        rightYAxis.setAxisMinimum(0f);
        //rightYAxis.setLabelCount(11,true)
        lineChart.getAxisRight().setEnabled(false);//隐藏右侧Y轴

        MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);//由于点击坐标点显示的布局
        mv.setChartView(lineChart); // For bounds control
        lineChart.setMarker(mv); // Set the marker to the chart;
        lineChart.getDescription().setEnabled(false);
        lineChart.setDragEnabled(true);
        // 是否可以缩放
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);


        Description description = new Description();  // 这部分是深度定制描述文本，颜色，字体等
        description.setText(unit);
        description.setTextSize(13);
        description.setTextColor(Color.RED);
        lineChart.setDescription(description);
        lineChart.setNoDataText("暂无数据");
        lineChart.setBackground(context.getResources().getDrawable(bg));
//        lineChart.setTouchEnabled(true); // 所有触摸事件,默认true
//        lineChart.setDragEnabled(true);    // 可拖动,默认true
//        lineChart.setScaleEnabled(true);   // 两个轴上的缩放,X,Y分别默认为true
        lineChart.setScaleXEnabled(true);  // X轴上的缩放,默认true
        lineChart.setScaleYEnabled(false);  // Y轴上的缩放,默认true
//        lineChart.setPinchZoom(true);  // X,Y轴同时缩放，false则X,Y轴单独缩放,默认false
        lineChart.setDoubleTapToZoomEnabled(false); // 双击缩放,默认true
//        lineChart.setDragDecelerationEnabled(true);    // 抬起手指，继续滑动,默认true
//        lineChart.setDragDecelerationFrictionCoef(0.9f);   // 摩擦系数,[0-1]，较大值速度会缓慢下降，0，立即停止;1,无效值，并转换为0.9999.默认0.9f.
        lineChart.setData(data);
    }


    /**
     * 设置LineDataSet对象
     *
     * @param entries 存放折线数据
     * @param lable   图例说明
     * @param lineColor   线颜色
     * @param CircleColor  坐标原点颜色
     * @return
     */
    public LineDataSet getLineDataSet(List<? extends BaseEntry> entries, String lable, int lineColor, int CircleColor) {

        LineDataSet lineDataSet = new LineDataSet((List<Entry>) entries, lable);
//        // 用y轴的集合来设置参数
//        // 不显示坐标点的数据
//        lineDataSet.setDrawValues(false);
//        // 显示坐标点的小圆点
//        lineDataSet.setDrawCircles(true);
//        // 定位线
//        lineDataSet.setHighlightEnabled(true);
//        // 线宽
//        lineDataSet.setLineWidth(2.0f);
//        // 显示的圆形大小
//        lineDataSet.setCircleSize(2f);
//        // 显示颜色
//        lineDataSet.setColor(context.getApplicationContext().getResources().getColor(R.color.bg_blue));
//        // 圆形的颜色
//        lineDataSet.setCircleColor(context.getApplicationContext().getResources().getColor(R.color.bg_blue));
//        // 高亮的线的颜色
//        lineDataSet.setHighLightColor(context.getApplicationContext().getResources()
//                .getColor(R.color.bg_blue));
//        // 设置坐标点的颜色
////        lineDataSet.setFillColor(context.getApplicationContext().getResources().getColor(R.color.bg_blue));
//        // 设置坐标点为空心环状
//        lineDataSet.setDrawCircleHole(false);
//        // lineDataSet.setValueTextSize(9f);
//        lineDataSet.setFillAlpha(65);
//        // 设置显示曲线和X轴围成的区域阴影
//        lineDataSet.setDrawFilled(false);
//        // 坐标轴在左侧
//        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
//        // 设置每条曲线图例标签名
//        // lineDataSet.setLabel("标签");
//        lineDataSet.setValueTextSize(14f);
//        // 曲线弧度（区间0.05f-1f，默认0.2f）
//        lineDataSet.setCubicIntensity(0.2f);
//        // 设置为曲线显示,false为折线


//        // 图例大小
//        mLegend.setFormSize(8f);
//        // 图例上的字体颜色
//        mLegend.setTextColor(context.getApplicationContext().getResources().getColor(R.color.bg_blue));
//        mLegend.setTextSize(12f);
//        // 图例字体
//        // mLegend.setTypeface(mTf);
//        // 图例的显示和隐藏
//        mLegend.setEnabled(true);
//        barChart.setDrawGridBackground(true); // 是否显示表格颜色
//        barChart.setGridBackgroundColor(Color.RED); // 表格的的颜色

        lineDataSet.setValueTextSize(0f);//坐标数值的字体大小
        lineDataSet.setCircleRadius(3f);//坐标圆的直径
        lineDataSet.setLineWidth(1.5f);//数据线宽度
        lineDataSet.setColor(lineColor);//数据线颜色 (可设置每一段线的颜色)

        lineDataSet.setCircleColor(CircleColor);
        // lineDataSet.enableDashedLine(10f, 5f, 0f);
        // lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);

        return lineDataSet;
    }


    /**
     * 显示柱状图
     * @param context 上下文
     * @param barChart 柱状对象
     * @param MaxVisibleValueCount 绘制条目数量 超过指定数量后将不再绘制
     * @param Granularity 数据间隔
     * @param xLabelCount x轴上的标签数量
     * @param AxisMinimum 最小值
     * @param AxisMaximum 最大值
     * @param leftLabelCount y轴上的标签数量
     * @param yVals1 柱状数据
     */
    public void ShowBarChart(Context context, BarChart barChart,int MaxVisibleValueCount,
                             float Granularity,int xLabelCount,float AxisMinimum,float AxisMaximum,int leftLabelCount,List<? extends BaseEntry> yVals1){

        BarDataSet barDataSet = null;
        if(barChart.getData()!=null && barChart.getData().getDataSetCount()>0){
             barDataSet = (BarDataSet)barChart.getData().getDataSetByIndex(0);
             barDataSet.setValues((List<BarEntry>) yVals1);
             barChart.getData().notifyDataChanged();
             barChart.notifyDataSetChanged();
        }else {

            barChart.setDrawBarShadow(false);
            barChart.setDrawValueAboveBar(true);

            barChart.getDescription().setEnabled(false);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            barChart.setMaxVisibleValueCount(MaxVisibleValueCount);

            // scaling can now only be done on x- and y-axis separately
            barChart.setPinchZoom(false);

            barChart.setDrawGridBackground(false);
            // mChart.setDrawYLabels(false);


            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(Granularity); // only intervals of 1 day
            xAxis.setLabelCount(xLabelCount);


            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setLabelCount(leftLabelCount, true);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);//设置在图表上最高处的值相比轴上最高值的顶端空间（总轴范围的百分比）
            leftAxis.setAxisMinimum(AxisMinimum); // this replaces setStartAtZero(true)
            leftAxis.setAxisMaximum(AxisMaximum);

            Legend l = barChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);
            // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
            // "def", "ghj", "ikl", "mno" });
            // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
            // "def", "ghj", "ikl", "mno" });

            barDataSet = new BarDataSet((List<BarEntry>) yVals1, "柱状图");

            barDataSet.setDrawIcons(false);

            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);//设置数据柱状颜色，可设置多种颜色（以数组形式存放颜色资源）
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(barDataSet);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);


            MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);//由于点击坐标点显示的布局
            mv.setChartView(barChart); // For bounds control
            barChart.getAxisRight().setEnabled(false);
            barChart.setMarker(mv); // Set the marker to the chart;
            barChart.setData(data);
        }
    }


}
