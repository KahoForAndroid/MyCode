package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.lang.reflect.Type;
import java.util.List;

import zj.health.health_v1.Model.Chart;
import zj.health.health_v1.Model.GraphicModel;
import zj.health.health_v1.Model.NewGraphicModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.ChartUtil;

/**
 * Created by Administrator on 2018/6/28.
 */

public class Weight_ChartFragment_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Chart> charts;
    private Context context;
    private Type t;
    private ChartUtil chartUtil;
    private NewGraphicModel newGraphicModel;
    private int graphicType;

    public Weight_ChartFragment_Adapter(Context context,List<Chart> charts,NewGraphicModel newGraphicModel){
        this.context = context;
        this.charts = charts;
        this.newGraphicModel = newGraphicModel;
        chartUtil = new ChartUtil();

    }

    public void setData(List<Chart> charts,NewGraphicModel newGraphicModel,int graphicType){
        this.charts = charts;
        this.newGraphicModel = newGraphicModel;
        this.graphicType = graphicType;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == 0){
            View view = LayoutInflater.from(context).inflate(R.layout.chart_recy_item,parent,false);
            viewHolder = new Weight_ChartFragment_Adapter.LineViewHolder(view);
        }else if(viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.barchart_recy_item,parent,false);
            viewHolder = new Weight_ChartFragment_Adapter.BarViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof LineViewHolder) {
            setWeightChart((LineViewHolder) holder, position);
        }
    }

    public void setWeightChart(LineViewHolder holder, int position){
        LineDataSet lineDataSet [] = new LineDataSet[charts.get(position).getEntriesList().size()];
        String lable[] = newGraphicModel.getLable().split(",");
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



        holder.title_text.setText(newGraphicModel.getTitle());
        float average1 = 0;

        //因为查年类型并没有下发平均值数据，所以这里需要判断
        if(graphicType == 3){
            if(newGraphicModel.getYear().size()>0) {
                average1 = Float.parseFloat(newGraphicModel.getYear().get(0).getAverage());
            }else{
                average1 = 0;
            }
        }else if(graphicType == 2){
            if(newGraphicModel.getMonthly().size()>0) {
                average1 = Float.parseFloat(newGraphicModel.getMonthly().get(0).getAverage());
            }else{
                average1 = 0;
            }
        }else if(graphicType == 1){
            if(newGraphicModel.getGraphicDayList().size()>0) {
                average1 = Float.parseFloat(newGraphicModel.getAverage());
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
//        try {
//            t = charts.get(position).getClass().getDeclaredField("entries").getGenericType();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//
//            if(((ParameterizedType) t).getActualTypeArguments()[0] instanceof Entry){
//                return 0;
//            }else if(((ParameterizedType) t).getActualTypeArguments()[0] instanceof BarEntry){
//                return 1;
//            }else{
//                return 0;
//            }
    }

    class LineViewHolder extends RecyclerView.ViewHolder{

        LineChart lineChart;
        TextView title_text;

        public LineViewHolder(View itemView) {
            super(itemView);
            lineChart = (LineChart)itemView.findViewById(R.id.lineChart);
            title_text = (TextView)itemView.findViewById(R.id.title_text);
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



