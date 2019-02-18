package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.BodyChartListModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;

/**
 * Created by Administrator on 2018/10/22.
 */

public class Body_ChartList_Adapter extends RecyclerView.Adapter<Body_ChartList_Adapter.ViewHolder>{

    private Context context;
    private List<BodyChartListModel> bodyChartListModelList;
    private int Type;
    private OnItemClick onItemClick;

    public Body_ChartList_Adapter(Context context,List<BodyChartListModel> bodyChartListModelList,int Type){
        this.context = context;
        this.bodyChartListModelList = bodyChartListModelList;
        this.Type = Type;
    }

    public void setBodyChartListModelList(List<BodyChartListModel> bodyChartListModelList){
        this.bodyChartListModelList = bodyChartListModelList;
        notifyDataSetChanged();
    }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.body_chartlist_item_layout,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            holder.time_text.setText(DateUtil.UTCToCST(bodyChartListModelList.get(position).getRecordTime()));
            holder.edit_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.OnItemClickListener(v,position);
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (Type){
            case 1:
                holder.body_text.setText(context.getString(R.string.systolic)+":"+bodyChartListModelList.get(position).getSystolicPressure());
                holder.body_text2.setText(context.getString(R.string.diastolic)+":"+bodyChartListModelList.get(position).getDiastolicPressure());
                holder.body_text3.setText(context.getString(R.string.heart_rate)+":"+bodyChartListModelList.get(position).getHeartRate());
                break;
            case 2:
                holder.body_text.setText(context.getString(R.string.weight)+":"+bodyChartListModelList.get(position).getWeight());
                if(bodyChartListModelList.get(position).getType().equals("1")){
                    holder.body_text2.setText(context.getString(R.string.meal));
                }else{
                    holder.body_text2.setText(context.getString(R.string.Postprandial));
                }
                break;
            case 3:
                holder.body_text.setText(context.getString(R.string.temperature)+":"+bodyChartListModelList.get(position).getTemp());
                break;
            case 4:
                holder.body_text.setText(context.getString(R.string.heart_rate)+":"+bodyChartListModelList.get(position).getHeartRate());
                if(bodyChartListModelList.get(position).getType().equals("1")){
                    holder.body_text2.setText(context.getString(R.string.Sport));
                }else{
                    holder.body_text2.setText(context.getString(R.string.Rest_in_rest));
                }
                break;
            case 5:
                holder.body_text.setText(context.getString(R.string.blood_sugar)+":"+bodyChartListModelList.get(position).getBloodGlucose());

                break;
                default:
                    break;
        }
    }

    @Override
    public int getItemCount() {
        return bodyChartListModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Type;
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView body_text,body_text2,body_text3,time_text,edit_text;

        public ViewHolder(View itemView) {
            super(itemView);
            body_text = (TextView)itemView.findViewById(R.id.body_text);
            body_text2 = (TextView)itemView.findViewById(R.id.body_text2);
            body_text3 = (TextView)itemView.findViewById(R.id.body_text3);
            time_text = (TextView)itemView.findViewById(R.id.time_text);
            edit_text = (TextView)itemView.findViewById(R.id.edit_text);
        }
    }
}
