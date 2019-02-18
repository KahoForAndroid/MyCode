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

import zj.health.health_v1.Model.CalendarNoticeModel;
import zj.health.health_v1.Model.ReminderListModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;

/**
 * Created by Administrator on 2018/6/15.
 */

public class TakeDrugTime_Adapter extends RecyclerView.Adapter<TakeDrugTime_Adapter.ViewHolder>{

    private Context context;
    private List<ReminderListModel> list;
    private int ParentPosition = 0;
    private boolean itemFocusable = true;


    public TakeDrugTime_Adapter(Context context, List<ReminderListModel> list, int ParentPosition){
        this.context = context;
        this.list = list;
        this.ParentPosition = ParentPosition;

    }
    public TakeDrugTime_Adapter(Context context, List<ReminderListModel> list, int ParentPosition,boolean itemFocusable){
        this.context = context;
        this.list = list;
        this.ParentPosition = ParentPosition;
        this.itemFocusable = itemFocusable;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.takedrugtime_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String pos = position+1+"";
            holder.drugtime_text.setText(context.getString(R.string.Times_of_taking_medicine)+pos);
            holder.time_text.setText(list.get(ParentPosition).getReminderTime().get(position));

        final TimePickerView.OnTimeSelectListener listener = new TimePickerView.OnTimeSelectListener(){

            @Override
            public void onTimeSelect(Date date, View v) {
                holder.time_text.setText(DateUtil.getDaysStrhm(date));
                list.get(ParentPosition).getReminderTime().set(position,DateUtil.getDaysStrhm(date));
                notifyDataSetChanged();
            }
        };
        final TimePickerView timePickerView = new DateUtil().ShowTimePickerView(context,listener, Calendar.getInstance(),
                false,false,false,true,true,false);

        holder.time_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemFocusable){
                    timePickerView.show();
                }

            }
        });


    }

    public void setList(List<ReminderListModel> list){
        this.list = list;
    }

    @Override
    public int getItemCount() {

        return list.get(ParentPosition).getReminderTime().size();
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
