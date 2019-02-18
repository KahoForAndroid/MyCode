package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BaseEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Fragment.ChartFragment;
import zj.health.health_v1.Model.Chart;
import zj.health.health_v1.Model.GraphicModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.ChartUtil;

/**
 * Created by Administrator on 2018/5/25.
 */

public class ChartFragment_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Chart> charts;
    private Context context;
    private Type t;
    private ChartUtil chartUtil;
    private GraphicModel.graphics graphics;
    private List<GraphicModel.graphics> graphicsList;
    private float systolic_pressure_average;//收缩压平均值（仅用于血压表）
    private float diastolic_pressure_average;//舒张压平均值（仅用于血压表）



    public ChartFragment_Adapter(Context context,List<Chart> charts,List<GraphicModel.graphics> graphicsList,
                                 float systolic_pressure_average,float diastolic_pressure_average){
        this.context = context;
        this.charts = charts;
        this.graphicsList = graphicsList;
        this.diastolic_pressure_average = diastolic_pressure_average;
        this.systolic_pressure_average = systolic_pressure_average;
         chartUtil = new ChartUtil();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == 0){
            View view = LayoutInflater.from(context).inflate(R.layout.chart_recy_item,parent,false);
            viewHolder = new LineViewHolder(view);
        }else if(viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.barchart_recy_item,parent,false);
            viewHolder = new BarViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof LineViewHolder){

            ((LineViewHolder) holder).title.setVisibility(View.VISIBLE);

            if(graphicsList.get(position).getTitle().equals("血糖")) {
                setBlood_sugarChart((LineViewHolder) holder,position);

            }else if(graphicsList.get(position).getTitle().equals("血压")){
                setBlood_pressure((LineViewHolder) holder,position);

            }else if(graphicsList.get(position).getTitle().equals("体重")){
                setWeightChart((LineViewHolder) holder,position);

            }else if(graphicsList.get(position).getTitle().equals("心率")){
                setHeartChart((LineViewHolder) holder,position);

            }else if(graphicsList.get(position).getTitle().equals("体温")){
                setTemperatureChart((LineViewHolder) holder,position);

            }

        }else if(holder instanceof BarViewHolder){//柱形图 预留
            chartUtil.ShowBarChart(context,((BarViewHolder) holder).barChart,60,1f,
                    7,0f,50f,7,charts.get(position).getEntries());

        }
    }

    @Override
    public int getItemCount() {
        return charts.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(charts.get(position).getType() == 0){
            return 0;
        }else{
            return 1;
        }

    }

    public void setBlood_sugarChart(LineViewHolder holder,int position){
        float increment;
        int ycount = 0;
        float number = Float.parseFloat(graphicsList.get(position).getGraphicEndRange())
                - Float.parseFloat(graphicsList.get(position).getGraphicStartRange());
        if (number > 5) {
            increment = 1;
        } else {
            increment = 0.5f;
        }

        float match = (Float.parseFloat(graphicsList.get(position).getGraphicStartRange()));
        boolean whileSet = true;
        while (whileSet) {
            ycount++;
            match += increment;
            if (match == (Float.parseFloat(graphicsList.get(position).getGraphicEndRange()))) {
                whileSet = false;
            }
        }

        ((LineViewHolder) holder).title_text.setText(graphicsList.get(position).getTitle());
        chartUtil.showLineChat(context, ((LineViewHolder) holder).lineChart, charts.get(position).getxStringType(),
                graphicsList.get(position).getList().get(0).getData().size(), ycount, Float.parseFloat(graphicsList.get(position).getGraphicStartRange()),
                Float.parseFloat(graphicsList.get(position).getGraphicEndRange()),"","",
                Float.parseFloat(graphicsList.get(position).getGraphicCenter()),0,context.getString(R.string.Graph_supplement),
                chartUtil.getLineDataSet(charts.get(position).getEntries(), graphicsList.get(position).getUnit()+context.getString(R.string.average_red),
                        context.getResources().getColor(R.color.line_color), context.getResources().getColor(R.color.bg_blue)));

    }



    public void setWeightChart(LineViewHolder holder,int position){
        float increment;
        int ycount = 0;
        float number = Float.parseFloat(graphicsList.get(position).getGraphicEndRange())
                - Float.parseFloat(graphicsList.get(position).getGraphicStartRange());

        if(number >= 60){
            increment = 10;
        }else if (number >= 40) {
            increment = 5;
        } else {
            increment = 2;
        }

        float match = (Float.parseFloat(graphicsList.get(position).getGraphicStartRange()));
        boolean whileSet = true;
        while (whileSet) {
            ycount++;
            match += increment;
            if (match == (Float.parseFloat(graphicsList.get(position).getGraphicEndRange()))) {
                whileSet = false;
            }
        }

        ((LineViewHolder) holder).title_text.setText(graphicsList.get(position).getTitle());
        chartUtil.showLineChats(context,
                ((LineViewHolder) holder).lineChart,
                charts.get(position).getxStringType(),
                graphicsList.get(position).getList().get(0).getData().size(),
                ycount,
                Float.parseFloat(graphicsList.get(position).getGraphicStartRange()),
                Float.parseFloat(graphicsList.get(position).getGraphicEndRange()),
                R.color.white,
                "",
                "",
                Float.parseFloat(graphicsList.get(position).getGraphicCenter()),
                0,
                context.getString(R.string.Graph_supplement),
                chartUtil.getLineDataSet(charts.get(position).getEntries(), graphicsList.get(position).getUnit()+context.getString(R.string.average_red),
                        context.getResources().getColor(R.color.line_color), context.getResources().getColor(R.color.bg_blue)));

    }

    public void setHeartChart(LineViewHolder holder,int position){
        float increment;
        int ycount = 0;
        float number = Float.parseFloat(graphicsList.get(position).getGraphicEndRange())
                - Float.parseFloat(graphicsList.get(position).getGraphicStartRange());
        if (number >= 200) {
            increment = 20;
        } else {
            increment = 10;
        }

        float match = (Float.parseFloat(graphicsList.get(position).getGraphicStartRange()));
        boolean whileSet = true;
        while (whileSet) {
            ycount++;
            match += increment;
            if (match == (Float.parseFloat(graphicsList.get(position).getGraphicEndRange()))) {
                whileSet = false;
            }
        }

        ((LineViewHolder) holder).title_text.setText(graphicsList.get(position).getTitle());
        chartUtil.showLineChat(context, ((LineViewHolder) holder).lineChart, charts.get(position).getxStringType(),
                graphicsList.get(position).getList().get(0).getData().size(), ycount, Float.parseFloat(graphicsList.get(position).getGraphicStartRange()),
                Float.parseFloat(graphicsList.get(position).getGraphicEndRange()),"","",
                Float.parseFloat(graphicsList.get(position).getGraphicCenter()),0,context.getString(R.string.Graph_supplement),
                chartUtil.getLineDataSet(charts.get(position).getEntries(), graphicsList.get(position).getUnit()+context.getString(R.string.average_red),
                        context.getResources().getColor(R.color.line_color), context.getResources().getColor(R.color.bg_blue)));

    }



    public void setTemperatureChart(LineViewHolder holder,int position){
        float increment;
        int ycount = 0;
        float number = Float.parseFloat(graphicsList.get(position).getGraphicEndRange())
                - Float.parseFloat(graphicsList.get(position).getGraphicStartRange());
        if (number > 5) {
            increment = 1;
        } else {
            increment = 0.5f;
        }

        float match = (Float.parseFloat(graphicsList.get(position).getGraphicStartRange()));
        boolean whileSet = true;
        while (whileSet) {
            ycount++;
            match += increment;
            if (match == (Float.parseFloat(graphicsList.get(position).getGraphicEndRange()))) {
                whileSet = false;
            }
        }

        ((LineViewHolder) holder).title_text.setText(graphicsList.get(position).getTitle());
        chartUtil.showLineChats(context,
                ((LineViewHolder) holder).lineChart,
                charts.get(position).getxStringType(),
                graphicsList.get(position).getList().get(0).getData().size(),
                ycount,
                Float.parseFloat(graphicsList.get(position).getGraphicStartRange()),
                Float.parseFloat(graphicsList.get(position).getGraphicEndRange()),
                R.color.white,
                "",
                "",
                Float.parseFloat(graphicsList.get(position).getGraphicCenter()),
                0,
                context.getString(R.string.Graph_supplement),
                chartUtil.getLineDataSet(charts.get(position).getEntries(), graphicsList.get(position).getUnit()
                                +context.getString(R.string.average_red),
                        context.getResources().getColor(R.color.line_color),
                        context.getResources().getColor(R.color.bg_blue)));

    }





    public void setBlood_pressure(LineViewHolder holder,int position){
        float increment;
        int ycount = 0;
        float number = Float.parseFloat(graphicsList.get(position).getGraphicEndRange())
                - Float.parseFloat(graphicsList.get(position).getGraphicStartRange());
        increment = 10;


        LineDataSet lineDataSet [] = new LineDataSet[charts.get(position).getEntriesList().size()];

        for (int i = 0;i<charts.get(position).getEntriesList().size();i++){
            if(i == 1){
                LineDataSet lineDataSet1 =  chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(i), graphicsList.get(position).getList().get(1).getTitle()
                                +context.getString(R.string.average_red),
                        context.getResources().getColor(R.color.bg_blue), context.getResources().getColor(R.color.line_color));
                lineDataSet[i] = lineDataSet1;
            }else{
                LineDataSet lineDataSet1 =  chartUtil.getLineDataSet((List<Entry>) charts.get(position).getEntriesList().get(i), graphicsList.get(position).getList().get(0).getTitle()
                                +context.getString(R.string.average_red),
                        context.getResources().getColor(R.color.line_color), context.getResources().getColor(R.color.bg_blue));
                lineDataSet[i] = lineDataSet1;

            }

        }
        float match = (Float.parseFloat(graphicsList.get(position).getGraphicStartRange()));
        boolean whileSet = true;
        while (whileSet) {
            ycount++;
            match += increment;
            if (match == (Float.parseFloat(graphicsList.get(position).getGraphicEndRange()))) {
                whileSet = false;
            }
        }



        ((LineViewHolder) holder).title_text.setText(graphicsList.get(position).getTitle());
        chartUtil.showLineChatForBlood_pressure(context, ((LineViewHolder) holder).lineChart, charts.get(position).getxStringType(),
                graphicsList.get(position).getList().get(0).getData().size(), ycount, Float.parseFloat(graphicsList.get(position).getGraphicStartRange()),
                Float.parseFloat(graphicsList.get(position).getGraphicEndRange()),graphicsList.get(position).getUnit()+context.getString(R.string.Graph_supplement),
                "",
                "",
                systolic_pressure_average,
                diastolic_pressure_average,
                lineDataSet);

    }

    class LineViewHolder extends RecyclerView.ViewHolder{

        LineChart lineChart;
        TextView title_text;
        LinearLayout title;

        public LineViewHolder(View itemView) {
            super(itemView);
            lineChart = (LineChart)itemView.findViewById(R.id.lineChart);
            title_text = (TextView)itemView.findViewById(R.id.title_text);
            title = (LinearLayout)itemView.findViewById(R.id.title);
        }
    }

    class BarViewHolder extends RecyclerView.ViewHolder{

        BarChart barChart;

        public BarViewHolder(View itemView) {
            super(itemView);
            barChart = (BarChart) itemView.findViewById(R.id.barChart);
        }
    }
}
