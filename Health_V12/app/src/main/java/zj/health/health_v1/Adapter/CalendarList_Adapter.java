package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.CalendarNoticeListModel;
import zj.health.health_v1.Model.RemindCalendarModel;
import zj.health.health_v1.Model.ReminderListModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;

/**
 * Created by Administrator on 2018/6/26.
 */

public class CalendarList_Adapter extends RecyclerView.Adapter<CalendarList_Adapter.ViewHolder>{

    private List<RemindCalendarModel> list;
    private Context context;
    private OnItemClick onItemClick;
    private String[] timesOneDayList;
    private String[] takeTimeList;
    private String[] dosageList;
    private String[] dosageUnitList;
    private String[] treatmentList;
    private String[] intervalModeList;
    private String selectDay;
    private boolean friendRemind = false;//是否从亲友日志进入

    public CalendarList_Adapter(Context context,List<RemindCalendarModel> list,String selectDay,boolean friendRemind){
        this.context = context;
        this.list = list;
        this.selectDay = selectDay;
        this.friendRemind = friendRemind;
        timesOneDayList = context.getResources().getStringArray(R.array.much_forday);
        takeTimeList = context.getResources().getStringArray(R.array.takeTime);
        dosageList = context.getResources().getStringArray(R.array.dosage);
        dosageUnitList = context.getResources().getStringArray(R.array.dosageUnit);
        treatmentList = context.getResources().getStringArray(R.array.treatment);
        intervalModeList = context.getResources().getStringArray(R.array.intervalMode);
    }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    public void setList(List<RemindCalendarModel> list,String selectDay){
        this.list = list;
        this.selectDay = selectDay;
         notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.calendarlist_recy_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String dateString = DateUtil.getDateStrYmd(new Date(Long.parseLong(list.get(position).getReminderDaily().get(0).getEventTime())));
        holder.time_text.setText(dateString);
        holder.calendar_list_recy_lin.removeAllViews();
        for (int i = 0;i<list.get(position).getReminderDaily().size();i++){
            if(DateUtil.getDateStrYmd(new Date(Long.parseLong(list.get(position).getReminderDaily().get(i).getEventTime()))).
                    equals(selectDay)){
                for (int j = 0;j<list.get(position).getReminderDaily().get(i).getEvents().size();j++) {
                    View view = View.inflate(context, R.layout.calendarlist_recy_item, null);
                    LinearLayout layout_lin = (LinearLayout) view.findViewById(R.id.layout_lin);
                    TextView drugname_text = (TextView) view.findViewById(R.id.drugname_text);
                    TextView meal_text = (TextView) view.findViewById(R.id.meal_text);
                    TextView drug_much_text = (TextView) view.findViewById(R.id.drug_much_text);
                    TextView drugtime_text = (TextView) view.findViewById(R.id.drugtime_text);
                    TextView take_drug_text = (TextView) view.findViewById(R.id.take_drug_text);
                    ImageView take_drug_img = (ImageView) view.findViewById(R.id.take_drug_img);
                    drugname_text.setText(list.get(position).getMedicineName());
                    meal_text.setText(takeTimeList[Integer.parseInt(list.get(position).getTakeTime()) - 1]);
                    drug_much_text.setText("一次" + dosageList[Integer.parseInt(list.get(position).getDosage()) - 1] + dosageUnitList[Integer.parseInt(list.get(position).getDosageUnit()) - 1]);
                    drugtime_text.setText(list.get(position).getReminderDaily().get(i).getEvents().get(j).getReminderTime());
                    take_drug_text.setVisibility(View.VISIBLE);
                    take_drug_img.setVisibility(View.GONE);
                    if (list.get(position).getReminderDaily().get(i).getEvents().get(j).isTake()) {
                        take_drug_text.setTextColor(context.getResources().getColor(R.color.item_green));
                        take_drug_text.setText(context.getString(R.string.take_drug_Already));
                    } else {
                        if (DateUtil.getDateStrYmd(new Date(Long.parseLong(list.get(position).getReminderDaily().get(i).getEventTime()))).
                                equals(DateUtil.getNowdayymd())) {
                            String nowDay = DateUtil.getNowDay() + ":00";
                            String dataTime = DateUtil.getNowdayymd() + " " + list.get(position).getReminderDaily().get(i).getEvents().get(j).getReminderTime() + ":00";
                            try {
                                if (DateUtil.dateToStamp_long(nowDay) < DateUtil.dateToStamp_long(dataTime)+1000 * 60 * 30) {
                                    if(friendRemind){
                                        take_drug_text.setVisibility(View.VISIBLE);
                                        take_drug_img.setVisibility(View.GONE);
                                        take_drug_text.setTextColor(context.getResources().getColor(R.color.colorGray5));
                                        take_drug_text.setText(context.getString(R.string.un_take_drug));
                                    }else{
                                        take_drug_text.setVisibility(View.GONE);
                                        take_drug_img.setVisibility(View.VISIBLE);
                                    }
                                }else{
                                    take_drug_text.setVisibility(View.VISIBLE);
                                    take_drug_img.setVisibility(View.GONE);
                                    take_drug_text.setTextColor(context.getResources().getColor(R.color.colorGray5));
                                    take_drug_text.setText(context.getString(R.string.un_take_drug));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            take_drug_text.setTextColor(context.getResources().getColor(R.color.colorGray5));
                            take_drug_text.setText(context.getString(R.string.un_take_drug));
                        }
                    }
                    final int finalI = i;
                    final int finalJ = j;
                    take_drug_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClick.itemclickListener(list.get(position).getId(), list.get(position).getReminderDaily().get(finalI).getEventTime(),
                                    list.get(position).getReminderDaily().get(finalI).getEvents().get(finalJ).getReminderTime(),true);
                        }
                    });
                    holder.calendar_list_recy_lin.addView(view);
                }
            }

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView time_text;
        LinearLayout calendar_list_recy_lin;

        public ViewHolder(View itemView) {
            super(itemView);
            time_text = (TextView)itemView.findViewById(R.id.time_text);
            calendar_list_recy_lin = (LinearLayout)itemView.findViewById(R.id.calendar_list_recy_lin);
        }
    }

    public interface OnItemClick{
        void itemclickListener(String reminderId,String eventTime,String reminderTime,boolean isDrug);
    }
}
