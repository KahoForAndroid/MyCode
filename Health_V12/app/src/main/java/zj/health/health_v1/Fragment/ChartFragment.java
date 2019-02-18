package zj.health.health_v1.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BaseEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zj.health.health_v1.Adapter.ChartFragment_Adapter;
import zj.health.health_v1.Adapter.New_Fragment_PagerAdapter;
import zj.health.health_v1.Adapter.Report_Adapter;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.Chart;
import zj.health.health_v1.Model.GraphicModel;
import zj.health.health_v1.MyView.CustomViewPager;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.ChartUtil;

/**
 * Created by Administrator on 2018/5/24.
 */

public class ChartFragment extends Fragment{

    private LineChart lineChart;
    private LineChart lineChart2;
    private com.github.mikephil.charting.charts.BarChart BarChart;
    private View view = null;
    private RecyclerView recyclerView;
    private ChartFragment_Adapter adapter;
    private List<Chart> charts;
    private List<GraphicModel.graphics> graphicsList = null;
    private RelativeLayout top_button_rela,backornext_layout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chart_fragment,container,false);
        initView();
        setData();
        return view;
    }

    private void initView(){

//        lineChart = (LineChart)view.findViewById(R.id.lineChart);
//        lineChart2 = (LineChart)view.findViewById(R.id.lineChart2);
//        BarChart = (com.github.mikephil.charting.charts.BarChart)view.findViewById(R.id.barChart);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        top_button_rela = (RelativeLayout)view.findViewById(R.id.top_button_rela);
        backornext_layout = (RelativeLayout)view.findViewById(R.id.backornext_layout);
    }

    private void setData(){
        top_button_rela.setVisibility(View.GONE);
        backornext_layout.setVisibility(View.GONE);
        graphicsList = (List<GraphicModel.graphics>) getArguments().getSerializable("graphicsList");
        charts = new ArrayList<>();

        float diastolic_pressure_average = 0;
        float systolic_pressure_average = 0;

        for (int position = 0;position<graphicsList.size();position++) {
            Chart chart = new Chart();
            List<String> xstringList = new ArrayList<>();
            List<Entry> entriesy = new ArrayList<>();
            if(graphicsList.get(position).getTitle().equals("血压")){
                List<List<? extends BaseEntry>> entriesList = new ArrayList<>();
                for (int j = 0;j<graphicsList.get(position).getList().size();j++){
                    List<Entry> entriesys = new ArrayList<>();
                    for (int i = 0; i < graphicsList.get(position).getList().get(j).getData().size(); i++) {
                        if(j == 0){
                            systolic_pressure_average += Float.parseFloat(graphicsList.get(position).getList().get(j).getData().get(i).getyPos().get(0));
                        }else{
                            diastolic_pressure_average += Float.parseFloat(graphicsList.get(position).getList().get(j).getData().get(i).getyPos().get(0));
                        }
                        if(j == 0){
                            xstringList.add(graphicsList.get(position).getList().get(j).getData().get(i).getxTitle());
                        }
                        if (Float.parseFloat(graphicsList.get(position).getList().get(j).getData().get(i).getyPos().get(0)) > 0) {
                            entriesys.add(new Entry(i, Float.valueOf(graphicsList.get(position).getList().get(j).getData().get(i).getyPos().get(0))));
                        }
                    }
                    if(j == 0){
                        systolic_pressure_average = systolic_pressure_average/entriesys.size();
                    }else{
                        diastolic_pressure_average = diastolic_pressure_average/entriesys.size();
                    }

                    entriesList.add(entriesys);
                }
                chart.setEntriesList(entriesList);

            }else{
                for (int i = 0; i < graphicsList.get(position).getList().get(0).getData().size(); i++) {
                    xstringList.add(graphicsList.get(position).getList().get(0).getData().get(i).getxTitle());
                    if (Float.parseFloat(graphicsList.get(position).getList().get(0).getData().get(i).getyPos().get(0)) > 0) {
                        entriesy.add(new Entry(i, Float.valueOf(graphicsList.get(position).getList().get(0).getData().get(i).getyPos().get(0))));
                    }
                }
                if(entriesy.size() == 0){
                    entriesy.add(new Entry(0, 0));
                }
                chart.setEntries(entriesy);
            }
            chart.setxStringType(xstringList);
            chart.setType(0);
            charts.add(chart);
        }

        adapter = new ChartFragment_Adapter(getActivity(),charts,graphicsList,systolic_pressure_average,diastolic_pressure_average);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

    }

//    private void setChatData(){
//        //设置数据
//        List<Entry> entries = new ArrayList<>();
//        for (int i = 0; i < 36; i++) {
//            entries.add(new Entry(i, (float) (Math.random()) * 25));
//        }
//        List<Entry> entries2 = new ArrayList<>();
//        for (int i = 0; i < 36; i++) {
//            entries2.add(new Entry(i, (float) (Math.random()) * 25));
//        }
//        String month [] = getResources().getStringArray(R.array.month);
//        int index = 0;
//        final List<String> monthlist = new ArrayList();
//        for(int i = 0;i<36;i++){
//            if(i%3 == 0){
//                monthlist.add(month[index]);
//                index ++;
//            }else{
//                monthlist.add(" ");
//            }
//
//        }
//
//        if (lineChart.getData() != null &&
//                lineChart.getData().getDataSetCount() > 0) {
//            for(int i = 0;i<lineChart.getData().getDataSets().size();i++){
//                LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSets().get(i);
//                lineDataSet.setValues(entries);
//                lineChart.getData().getDataSets().set(i,lineDataSet);
//            }
//            lineChart.getData().notifyDataChanged();
//            lineChart.notifyDataSetChanged();
//        } else{
//            ChartUtil chartUtil = new ChartUtil();
//            chartUtil.showLineChat(getActivity(),lineChart,monthlist,
//                    36,6,0f,25f,chartUtil.getLineDataSet(entries,"",
//                            R.color.line_color,getResources().getColor(R.color.bg_blue)));
//        }
//
//
//        if (lineChart2.getData() != null &&
//                lineChart2.getData().getDataSetCount() > 0) {
//            for(int i = 0;i<lineChart2.getData().getDataSets().size();i++){
//                LineDataSet lineDataSet = (LineDataSet) lineChart2.getData().getDataSets().get(i);
//                lineDataSet.setValues(entries);
//                lineChart2.getData().getDataSets().set(i,lineDataSet);
//            }
//            lineChart2.getData().notifyDataChanged();
//            lineChart2.notifyDataSetChanged();
//        } else{
//            ChartUtil chartUtil = new ChartUtil();
//            chartUtil.showLineChat(getActivity(),lineChart2,monthlist,
//                    36,6,0f,25f,
//                    chartUtil.getLineDataSet(entries,"",
//                            getResources().getColor(R.color.line_color),getResources().getColor(R.color.bg_blue)),
//                    chartUtil.getLineDataSet(entries2,"",
//                            getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.text_gray2)));
//
//
//            List<BarEntry> yVals1 = new ArrayList<BarEntry>();
//            for(int i = 0;i<7;i++){
//                float val = (float) (Math.random() * 45);
//                yVals1.add(new BarEntry(i,val));
//            }
//            chartUtil.ShowBarChart(getActivity(),BarChart,60,1f,
//                    7,0f,50f,7,yVals1);
//
//        }
//    }
}
