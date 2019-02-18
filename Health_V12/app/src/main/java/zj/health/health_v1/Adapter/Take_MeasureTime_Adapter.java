package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import zj.health.health_v1.Model.CalendarMeasureNotice;
import zj.health.health_v1.Model.CalendarNoticeModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;

/**
 * Created by Administrator on 2018/6/21.
 */

public class Take_MeasureTime_Adapter extends RecyclerView.Adapter<Take_MeasureTime_Adapter.ViewHolder>{

    private Context context;
    private List<CalendarMeasureNotice> list;
    private int ParentPosition = 0;
    private int size = 0;

    public Take_MeasureTime_Adapter(Context context, List<CalendarMeasureNotice> list, int ParentPosition){
        this.context = context;
        this.list = list;
        this.ParentPosition = ParentPosition;
    }

    @Override
    public Take_MeasureTime_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.takedrugtime_layout,parent,false);
        Take_MeasureTime_Adapter.ViewHolder viewHolder = new Take_MeasureTime_Adapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final Take_MeasureTime_Adapter.ViewHolder holder, final int position) {
        if(position == 0){
            holder.drugtime_text.setText(list.get(ParentPosition).getInfoAry().get(3).getlTitle());
            holder.time_text.setText(list.get(ParentPosition).getInfoAry().get(3).getrText());
        }else{
            holder.drugtime_text.setText(list.get(ParentPosition).getInfoAry().get(3+position).getlTitle());
            holder.time_text.setText(list.get(ParentPosition).getInfoAry().get(3+position).getrText());
        }
        final TimePickerView.OnTimeSelectListener listener = new TimePickerView.OnTimeSelectListener(){

            @Override
            public void onTimeSelect(Date date, View v) {
                holder.time_text.setText(DateUtil.getDaysStrhm(date));
                list.get(ParentPosition).getInfoAry().get(3+position).setrText(DateUtil.getDaysStrhm(date));
            }
        };
        final TimePickerView timePickerView = new DateUtil().ShowTimePickerView(context,listener, Calendar.getInstance(),
                false,false,false,true,true,false);;

        holder.time_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerView.show();
            }
        });




    }

    public void setList(List<CalendarMeasureNotice> list){
        this.list = list;
    }

    @Override
    public int getItemCount() {
        size = 0;
        for (int i = 3;i<6;i++){
            if(list.get(ParentPosition).getInfoAry().get(i).getIsShow()){
                size++;
            }
        }
        return size;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView drugtime_text,time_text;
        RelativeLayout time_rela;

        public ViewHolder(View itemView) {
            super(itemView);
            drugtime_text = (TextView)itemView.findViewById(R.id.drugtime_text);
            time_text = (TextView)itemView.findViewById(R.id.time_text);
            time_rela = (RelativeLayout)itemView.findViewById(R.id.time_rela);

        }
    }
}
