package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import zj.health.health_v1.Model.Chart;
import zj.health.health_v1.Model.FriendGraphicDayModel;
import zj.health.health_v1.Model.FriendGraphicModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.ChartUtil;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/10/25.
 */

public class FriendGraph_Adapter extends RecyclerView.Adapter<FriendGraph_Adapter.ViewHolder>{

    private Context context;
    private List<FriendGraphicModel.TypeData> list;
    private List<FriendGraphicDayModel> DayList;
    private List<Chart> charts;
    private ChartUtil chartUtil;
    private int graphicType;
    private int size = 0;

    public FriendGraph_Adapter(Context context,List<FriendGraphicModel.TypeData> list,List<Chart> charts){
        this.context = context;
        this.list = list;
        this.charts = charts;
        chartUtil = new ChartUtil();
    }

    public void setDataSize(List<FriendGraphicModel.TypeData> list,List<Chart> charts,int graphicType,int size,List<FriendGraphicDayModel> DayList){
        this.list = list;
        this.charts = charts;
        this.graphicType = graphicType;
        this.size = size;
        this.DayList = DayList;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.friend_chart_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(list != null && list.size()>0){
            holder.title_text.setText(StringUtil.trimNull(list.get(position).getTitle()));
        }else{
            holder.title_text.setText(StringUtil.trimNull(DayList.get(position).getTitle()));
        }

        if(list!=null && list.size()>0){
            if(list.get(position).getType() == 1){
                setBlood_pressure(holder,position);
            }else if(list.get(position).getType() == 2){
                setWeight(holder,position);
            }else if(list.get(position).getType() == 3){
                setTemp(holder,position);
            }else if(list.get(position).getType() == 4){
                setHeart_Rate(holder,position);
            }else if(list.get(position).getType() == 5){
                setSugar(holder,position);
            }
        }else{
            if(DayList.get(position).getType() == 1){
                setBlood_pressure(holder,position);
            }else if(DayList.get(position).getType() == 2){
                setWeight(holder,position);
            }else if(DayList.get(position).getType() == 3){
                setTemp(holder,position);
            }else if(DayList.get(position).getType() == 4){
                setHeart_Rate(holder,position);
            }else if(DayList.get(position).getType() == 5){
                setSugar(holder,position);
            }
        }
    }



    public void setBlood_pressure(ViewHolder holder, int position){

        LineDataSet lineDataSet [] = new LineDataSet[charts.get(position).getEntriesList().size()];
//        String lable[] = list.get(position).getLable().split(",");
        String lable[] = new String[]{context.getString(R.string.systolic),context.getString(R.string.diastolic)};
        if(lable.length>1) {
            LineDataSet lineDataSet1 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(0), lable[0]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.bg_blue), context.getResources().getColor(R.color.line_color));
            lineDataSet[0] = lineDataSet1;

            LineDataSet lineDataSet2 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(1), lable[1]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.line_color), context.getResources().getColor(R.color.bg_blue));
            lineDataSet[1] = lineDataSet2;

        }else{
            LineDataSet lineDataSet1 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(0), lable[0]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.bg_blue), context.getResources().getColor(R.color.line_color));
            lineDataSet[0] = lineDataSet1;
        }


        float average1 = 0;
        float average2 = 0;

        //因为查年类型并没有下发平均值数据，所以这里需要判断
        if(graphicType == 3){
            if(list.get(position).getYear()!=null && list.get(position).getYear().size()>0) {
                average1 = Float.parseFloat(list.get(position).getYear().get(0).getSysAverage());
                average2 = Float.parseFloat(list.get(position).getYear().get(0).getDiaAverage());
            }else{
                average1 = 0;
                average2 = 0;
            }
        }else if(graphicType == 2){
            if(list.get(position).getMonthly()!=null && list.get(position).getMonthly().size()>0) {
                average1 = Float.parseFloat(list.get(position).getMonthly().get(0).getSysAverage());
                average2 = Float.parseFloat(list.get(position).getMonthly().get(0).getDiaAverage());
            }else{
                average1 = 0;
                average2 = 0;
            }
        }else if(graphicType == 1){
            if(DayList.get(position).getBlood_pressure()!=null && DayList.get(position).getBlood_pressure().size()>0 && DayList.get(position).getBlood_pressure().get(0).getSystolicPressure()!=null) {
                float sysTotal = 0;//日收缩压平均值
                float diaTotal = 0;//日舒张压平均值
                for (int i = 0;i<DayList.get(position).getBlood_pressure().size();i++){
                    sysTotal += Float.parseFloat(DayList.get(position).getBlood_pressure().get(i).getSystolicPressure());
                    diaTotal += Float.parseFloat(DayList.get(position).getBlood_pressure().get(i).getDiastolicPressure());
                }
                average1 = sysTotal/DayList.get(position).getBlood_pressure().size();
                average2 = diaTotal/DayList.get(position).getBlood_pressure().size();
            }else {
                average1 = 0;
                average2 = 0;
            }
        }
        chartUtil.showLineChatForBlood_pressure(context, holder.lineChart, charts.get(position).getxStringType(),
                charts.get(position).getxStringType().size(), 13, 20,
                140,"",
                "",
                "",
                average1,
                average2,
                lineDataSet);

    }


    public void setWeight(ViewHolder holder, int position){

        LineDataSet lineDataSet [] = new LineDataSet[charts.get(position).getEntriesList().size()];
//        String lable[] = list.get(position).getLable().split(",");
        String lable[] = new String[]{"公斤"};
        if(lable.length>1) {
            LineDataSet lineDataSet1 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(0), lable[0]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.bg_blue), context.getResources().getColor(R.color.line_color));
            lineDataSet[0] = lineDataSet1;

            LineDataSet lineDataSet2 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(1), lable[1]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.line_color), context.getResources().getColor(R.color.bg_blue));
            lineDataSet[1] = lineDataSet2;

        }else{
            LineDataSet lineDataSet1 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(0), lable[0]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.bg_blue), context.getResources().getColor(R.color.line_color));
            lineDataSet[0] = lineDataSet1;
        }


        float average1 = 0;


        //因为查年类型并没有下发平均值数据，所以这里需要判断
        if(graphicType == 3){
            if(list.get(position).getYear()!=null && list.get(position).getYear().size()>0) {
                average1 = Float.parseFloat(list.get(position).getYear().get(0).getAverage());
            }else{
                average1 = 0;
            }
        }else if(graphicType == 2){
            if(list.get(position).getMonthly()!=null && list.get(position).getMonthly().size()>0) {
                average1 = Float.parseFloat(list.get(position).getMonthly().get(0).getAverage());
            }else{
                average1 = 0;
            }
        }else if(graphicType == 1){
            if(DayList.get(position).getWeight()!=null && DayList.get(position).getWeight().size()>0 && DayList.get(position).getWeight().get(0).getVal()!=null) {
                float val = 0;//日收缩压平均值

                for (int i = 0;i<DayList.get(position).getWeight().size();i++){
                    val += Float.parseFloat(DayList.get(position).getWeight().get(i).getVal());
                }
                average1 = val/DayList.get(position).getWeight().size();
            }else {
                average1 = 0;
            }
        }
        chartUtil.showLineChat(context, holder.lineChart, charts.get(position).getxStringType(),
                charts.get(position).getxStringType().size(), 12, 10,
                120,"","",
                average1,0,"",
                lineDataSet);
    }



    public void setTemp(ViewHolder holder, int position){

        LineDataSet lineDataSet [] = new LineDataSet[charts.get(position).getEntriesList().size()];
//        String lable[] = list.get(position).getLable().split(",");
        String lable[] = new String[]{"度"};
        if(lable.length>1) {
            LineDataSet lineDataSet1 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(0), lable[0]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.bg_blue), context.getResources().getColor(R.color.line_color));
            lineDataSet[0] = lineDataSet1;

            LineDataSet lineDataSet2 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(1), lable[1]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.line_color), context.getResources().getColor(R.color.bg_blue));
            lineDataSet[1] = lineDataSet2;

        }else{
            LineDataSet lineDataSet1 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(0), lable[0]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.bg_blue), context.getResources().getColor(R.color.line_color));
            lineDataSet[0] = lineDataSet1;
        }


        float average1 = 0;


        //因为查年类型并没有下发平均值数据，所以这里需要判断
        if(graphicType == 3){
            if(list.get(position).getYear()!=null && list.get(position).getYear().size()>0) {
                average1 = Float.parseFloat(list.get(position).getYear().get(0).getAverage());
            }else{
                average1 = 0;
            }
        }else if(graphicType == 2){
            if(list.get(position).getMonthly()!=null && list.get(position).getMonthly().size()>0) {
                average1 = Float.parseFloat(list.get(position).getMonthly().get(0).getAverage());
            }else{
                average1 = 0;
            }
        }else if(graphicType == 1){
            if(DayList.get(position).getTemp()!=null && DayList.get(position).getTemp().size()>0 && DayList.get(position).getTemp().get(0).getVal()!=null) {
                float val = 0;//日收缩压平均值

                for (int i = 0;i<DayList.get(position).getTemp().size();i++){
                    val += Float.parseFloat(DayList.get(position).getTemp().get(i).getVal());
                }
                average1 = val/DayList.get(position).getTemp().size();
            }else {
                average1 = 0;
            }
        }
        chartUtil.showLineChat(context, holder.lineChart, charts.get(position).getxStringType(),
                charts.get(position).getxStringType().size(), 10, 4,
                40,"","",
                average1,0,"",
                lineDataSet);
    }


    public void setHeart_Rate(ViewHolder holder, int position){

        LineDataSet lineDataSet [] = new LineDataSet[charts.get(position).getEntriesList().size()];
//        String lable[] = list.get(position).getLable().split(",");
        String lable[] = new String[]{"次"};
        if(lable.length>1) {
            LineDataSet lineDataSet1 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(0), lable[0]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.bg_blue), context.getResources().getColor(R.color.line_color));
            lineDataSet[0] = lineDataSet1;

            LineDataSet lineDataSet2 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(1), lable[1]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.line_color), context.getResources().getColor(R.color.bg_blue));
            lineDataSet[1] = lineDataSet2;

        }else{
            LineDataSet lineDataSet1 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(0), lable[0]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.bg_blue), context.getResources().getColor(R.color.line_color));
            lineDataSet[0] = lineDataSet1;
        }


        float average1 = 0;


        //因为查年类型并没有下发平均值数据，所以这里需要判断
        if(graphicType == 3){
            if(list.get(position).getYear()!=null && list.get(position).getYear().size()>0) {
                average1 = Float.parseFloat(list.get(position).getYear().get(0).getAverage());
            }else{
                average1 = 0;
            }
        }else if(graphicType == 2){
            if(list.get(position).getMonthly()!=null && list.get(position).getMonthly().size()>0) {
                average1 = Float.parseFloat(list.get(position).getMonthly().get(0).getAverage());
            }else{
                average1 = 0;
            }
        }else if(graphicType == 1){
            if(DayList.get(position).getHeart_rate()!=null && DayList.get(position).getHeart_rate().size()>0 && DayList.get(position).getHeart_rate().get(0).getVal()!=null) {
                float val = 0;//日收缩压平均值

                for (int i = 0;i<DayList.get(position).getHeart_rate().size();i++){
                    val += Float.parseFloat(DayList.get(position).getHeart_rate().get(i).getVal());
                }
                average1 = val/DayList.get(position).getHeart_rate().size();
            }else {
                average1 = 0;
            }
        }
        chartUtil.showLineChat(context, holder.lineChart, charts.get(position).getxStringType(),
                charts.get(position).getxStringType().size(), 13, 20,
                140,"","",
                average1,0,"",
                lineDataSet);
    }


    public void setSugar(ViewHolder holder, int position){

        LineDataSet lineDataSet [] = new LineDataSet[charts.get(position).getEntriesList().size()];
//        String lable[] = list.get(position).getLable().split(",");
        String lable[] = new String[]{context.getString(R.string.Mmol_L)};
        if(lable.length>1) {
            LineDataSet lineDataSet1 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(0), lable[0]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.bg_blue), context.getResources().getColor(R.color.line_color));
            lineDataSet[0] = lineDataSet1;

            LineDataSet lineDataSet2 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(1), lable[1]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.line_color), context.getResources().getColor(R.color.bg_blue));
            lineDataSet[1] = lineDataSet2;

        }else{
            LineDataSet lineDataSet1 = chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(0), lable[0]
                            + context.getString(R.string.average_red),
                    context.getResources().getColor(R.color.bg_blue), context.getResources().getColor(R.color.line_color));
            lineDataSet[0] = lineDataSet1;
        }


        float average1 = 0;


        //因为查年类型并没有下发平均值数据，所以这里需要判断
        if(graphicType == 3){
            if(list.get(position).getYear()!=null && list.get(position).getYear().size()>0) {
                average1 = Float.parseFloat(list.get(position).getYear().get(0).getAverage());
            }else{
                average1 = 0;
            }
        }else if(graphicType == 2){
            if(list.get(position).getMonthly()!=null && list.get(position).getMonthly().size()>0) {
                average1 = Float.parseFloat(list.get(position).getMonthly().get(0).getAverage());
            }else{
                average1 = 0;
            }
        }else if(graphicType == 1){
            if(DayList.get(position).getBlood_glucose()!=null && DayList.get(position).getBlood_glucose().size()>0 && DayList.get(position).getBlood_glucose().get(0).getVal()!=null) {
                float val = 0;//日收缩压平均值

                for (int i = 0;i<DayList.get(position).getBlood_glucose().size();i++){
                    val += Float.parseFloat(DayList.get(position).getBlood_glucose().get(i).getVal());
                }
                average1 = val/DayList.get(position).getBlood_glucose().size();
            }else {
                average1 = 0;
            }
        }
        chartUtil.showLineChat(context, holder.lineChart, charts.get(position).getxStringType(),
                charts.get(position).getxStringType().size(), 20, 1,
                20,"","",
                average1,0,"",
                lineDataSet);

    }

    @Override
    public int getItemCount() {
        return size;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        LineChart lineChart;
        TextView title_text;

        public ViewHolder(View itemView) {
            super(itemView);
            lineChart = (LineChart)itemView.findViewById(R.id.lineChart);
            title_text = (TextView)itemView.findViewById(R.id.title_text);
        }
    }
}
