package zj.health.health_v1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zj.health.health_v1.Activity.Add_drug_for_calendar_Activity;
import zj.health.health_v1.Activity.Calendar_Take_medicine_repeat_Activity;
import zj.health.health_v1.Activity.Prescription_Item_Details_Activity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.ReminderListModel;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/10/11.
 */

public class Prescr_Users_Details_Adapter extends RecyclerView.Adapter<Prescr_Users_Details_Adapter.ViewHolder>{

    private Context context;
    private List<ReminderListModel> list = new ArrayList<>();
    private OnItemClick onItemClick;
    private List<String> deleteIdlist = new ArrayList<>();
    private String[] timesOneDayList;
    private String[] takeTimeList;
    private String[] dosageList;
    private String[] dosageUnitList;
    private String[] treatmentList;
    private String[] intervalModeList;


    public Prescr_Users_Details_Adapter(Context context,List<ReminderListModel> list){
        this.context = context;
        this.list = list;
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

    public List<String> getDeleteId(){
        return deleteIdlist;
    }

    public List<ReminderListModel> getlist(){
        return list;
    }

    @Override
    public Prescr_Users_Details_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.prescription_recy_adapter_layout,parent,false);
        Prescr_Users_Details_Adapter.ViewHolder viewHolder = new Prescr_Users_Details_Adapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final Prescr_Users_Details_Adapter.ViewHolder holder, final int position) {

        holder.delete_rela.setVisibility(View.GONE);
        holder.startDate_text.setText(DateUtil.getNowdayymd());

        //设置药品名称数据
        if (StringUtil.isEmpty(list.get(position).getMedicineName())) {
            holder.drug_name_text.setText(context.getString(R.string.please_input));
        } else {
            holder.drug_name_text.setText(list.get(position).getMedicineName());
        }


        if(!StringUtil.isEmpty(list.get(position).getTakeTime()) && list.get(position).getTakeTime().equals("1")){
            holder.circle_img1.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_tick));
            holder.circle_img2.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_hollow));
        }else{
            list.get(position).setTakeTime("2");
            holder.circle_img2.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_tick));
            holder.circle_img1.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_hollow));
        }

        if(!StringUtil.isEmpty(list.get(position).getTimesOneDay())){
            holder.drug_Takemuch_text.setText(timesOneDayList[Integer.parseInt(list.get(position).getTimesOneDay())-1]);
        }
        if(!StringUtil.isEmpty(list.get(position).getDosage())){
            if(list.get(position).getDosageUnit().equals("0")){
                holder.dose_text.setText("一次"+dosageList[Integer.parseInt(list.get(position).getDosage())-1]);
            }else{
                holder.dose_text.setText("一次"+dosageList[Integer.parseInt(list.get(position).getDosage())-1]+dosageUnitList[Integer.parseInt(list.get(position).getDosageUnit())-1]);
            }
        }
        if(!StringUtil.isEmpty(list.get(position).getTreatment())){
            holder.Course_of_treatment_text.setText(treatmentList[Integer.parseInt(list.get(position).getTreatment())-1]);
        }


        final CreateDialog dialog = new CreateDialog();
        final OptionsPickerView optionsPickerView = null;
        if(list.get(position).getReminderTime() == null || list.get(position).getReminderTime().size()<=0){
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.YEAR,Calendar.MONTH+1,Calendar.DAY_OF_MONTH,17,0,0);
//            long times = calendar.getTimeInMillis() - DateUtil.initDateByDay().getTime();
//            list.get(position).setReminderTime(new ArrayList<String>());
//            list.get(position).getReminderTime().add(times+"");
            list.get(position).setReminderTime(new ArrayList<String>());
            list.get(position).getReminderTime().add("7:00");
            list.get(position).setTimesOneDay(1+"");
            holder.drug_Takemuch_text.setText(timesOneDayList[0]);
        }

        final TakeDrugTime_Adapter takeDrugTime_adapter = new TakeDrugTime_Adapter(context,list,position,false);
        holder.take_durg_time_recy.setLayoutManager(new LinearLayoutManager(context));
        holder.take_durg_time_recy.setAdapter(takeDrugTime_adapter);

    }

    public void setViewList(List<ReminderListModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout delete_rela,drug_name_rela,much_medicine_rela,dose_rela,Course_of_treatment_rela,startDate_rela;
        RecyclerView take_durg_time_recy;
        TextView drug_name_text,drug_Takemuch_text,dose_text,
                Course_of_treatment_text,startDate_text;
        ImageView circle_img2,circle_img1;
        Button delete_Button;

        public ViewHolder(View itemView) {
            super(itemView);
            delete_rela = (RelativeLayout)itemView.findViewById(R.id.delete_rela);
            delete_Button =(Button)itemView.findViewById(R.id.delete_Button);
            drug_name_rela = (RelativeLayout)itemView.findViewById(R.id.drug_name_rela);
            much_medicine_rela = (RelativeLayout)itemView.findViewById(R.id.much_medicine_rela);
            dose_rela = (RelativeLayout)itemView.findViewById(R.id.dose_rela);
            Course_of_treatment_rela = (RelativeLayout)itemView.findViewById(R.id.Course_of_treatment_rela);
            take_durg_time_recy = (RecyclerView)itemView.findViewById(R.id.take_durg_time_recy);
            drug_name_text = (TextView)itemView.findViewById(R.id.drug_name_text);
            drug_Takemuch_text = (TextView)itemView.findViewById(R.id.drug_Takemuch_text);
            dose_text = (TextView)itemView.findViewById(R.id.dose_text);
            Course_of_treatment_text = (TextView)itemView.findViewById(R.id.Course_of_treatment_text);
            startDate_text = (TextView)itemView.findViewById(R.id.startDate_text);
            circle_img2 = (ImageView)itemView.findViewById(R.id.circle_img2);
            circle_img1 = (ImageView)itemView.findViewById(R.id.circle_img1);
            startDate_rela = (RelativeLayout)itemView.findViewById(R.id.startDate_rela);

        }
    }

}

