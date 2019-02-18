package zj.health.health_v1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import zj.health.health_v1.Activity.Take_medicine_Activity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.ReminderListModel;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/10/11.
 */

public class Prescr_Details_Adapter extends RecyclerView.Adapter<Prescr_Details_Adapter.ViewHolder>{

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


    public Prescr_Details_Adapter(Context context,List<ReminderListModel> list){
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
    public Prescr_Details_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.prescription_recy_adapter_layout,parent,false);
        Prescr_Details_Adapter.ViewHolder viewHolder = new Prescr_Details_Adapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final Prescr_Details_Adapter.ViewHolder holder, final int position) {

        holder.startDate_rela.setVisibility(View.GONE);
//        holder.applyIllness_rela.setVisibility(View.VISIBLE);
        if(StringUtil.isEmpty(list.get(0).getId()) && position == 0){
            holder.delete_rela.setVisibility(View.GONE);
        }else{
            holder.delete_rela.setVisibility(View.VISIBLE);
        }

//        if(!StringUtil.isEmpty(list.get(position).getId())){
//            holder.delete_rela.setVisibility(View.GONE);
//        }else{
//            holder.delete_rela.setVisibility(View.VISIBLE);
//        }

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

        final TakeDrugTime_Adapter takeDrugTime_adapter = new TakeDrugTime_Adapter(context,list,position);
        holder.take_durg_time_recy.setLayoutManager(new LinearLayoutManager(context));
        holder.take_durg_time_recy.setAdapter(takeDrugTime_adapter);





        //服药次数点击监听
        final View.OnClickListener onDayMuch_ClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.day_one_rela:
//                        List<CalendarNoticeModel.infoAry> infoAries
                        list.get(position).setTimesOneDay(1+"");
                        dialog.getPopupWindow().dismiss();
                        holder.drug_Takemuch_text.setText(timesOneDayList[0]);
//                        for (int i =2;i<6;i++){
//                            if(i == 2){
//                                list.get(position).getInfoAry().get(i).setIsShow(true);
//                            }else{
//                                list.get(position).getInfoAry().get(i).setIsShow(false);
//                            }
//                        }
                        list.get(position).getReminderTime().clear();
                        list.get(position).getReminderTime().add("7:00");
                        takeDrugTime_adapter.setList(list);
                        takeDrugTime_adapter.notifyDataSetChanged();
                        notifyDataSetChanged();
                        break;
                    case R.id.day_two_rela:
                        list.get(position).setTimesOneDay(2+"");
                        dialog.getPopupWindow().dismiss();
                        holder.drug_Takemuch_text.setText(timesOneDayList[1]);
                        list.get(position).getReminderTime().clear();
                        list.get(position).getReminderTime().add("7:00");
                        list.get(position).getReminderTime().add("12:00");
                        takeDrugTime_adapter.setList(list);
                        takeDrugTime_adapter.notifyDataSetChanged();
                        notifyDataSetChanged();
                        break;
                    case R.id.day_three_rela:
                        list.get(position).setTimesOneDay(3+"");
                        dialog.getPopupWindow().dismiss();
                        holder.drug_Takemuch_text.setText(timesOneDayList[2]);
                        list.get(position).getReminderTime().clear();
                        list.get(position).getReminderTime().add("7:00");
                        list.get(position).getReminderTime().add("12:00");
                        list.get(position).getReminderTime().add("17:00");
                        takeDrugTime_adapter.setList(list);
                        takeDrugTime_adapter.notifyDataSetChanged();
                        notifyDataSetChanged();
                        break;
                    case R.id.day_four_rela:
                        list.get(position).setTimesOneDay(4+"");
                        dialog.getPopupWindow().dismiss();
                        holder.drug_Takemuch_text.setText(timesOneDayList[3]);
                        list.get(position).getReminderTime().clear();
                        list.get(position).getReminderTime().add("7:00");
                        list.get(position).getReminderTime().add("12:00");
                        list.get(position).getReminderTime().add("17:00");
                        list.get(position).getReminderTime().add("21:00");
                        takeDrugTime_adapter.setList(list);
                        takeDrugTime_adapter.notifyDataSetChanged();
                        notifyDataSetChanged();
                        break;
                    case R.id.cancel_rela:
                        dialog.getPopupWindow().dismiss();
                        break;
                    default:
                        break;
                }
            }
        };
        holder.delete_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(!StringUtil.isEmpty(list.get(position).getId()) && !list.get(position).getId().equals("0")){
//                    deleteIdlist.add(list.get(position).getId());
//                }
                if(StringUtil.isEmpty(list.get(position).getId())){
                    list.remove(position);
                    notifyDataSetChanged();
                }else{
                    onItemClick.OnItemClickListener(view,position);
                }


            }
        });
        holder.drug_name_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(context, Add_drug_for_calendar_Activity.class);
                it.putExtra("position",position);
                ((Prescription_Item_Details_Activity)context).startActivityForResult(it,0x1);
            }
        });
        holder.much_medicine_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> much_list = Arrays.asList(timesOneDayList);
                dialog.Medicine_much_Dailog(context, onDayMuch_ClickListener,android.R.style.Animation_Toast,much_list,position);
            }
        });
        holder.dose_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPickView(optionsPickerView,list,holder,position);
            }
        });
        holder.Course_of_treatment_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(context, Calendar_Take_medicine_repeat_Activity.class);
                it.putExtra("position",position);
                it.putExtra("name",list.get(position).getTreatment());
                ((Prescription_Item_Details_Activity)context).startActivityForResult(it,0x3);
            }
        });
        holder.circle_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.circle_img1.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_tick));
                holder.circle_img2.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_hollow));
                list.get(position).setTakeTime("1");

            }
        });
        holder.circle_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.circle_img2.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_tick));
                holder.circle_img1.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_hollow));
                list.get(position).setTakeTime("2");
            }
        });





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

    private void ShowPickView(OptionsPickerView optionsPickerView, final List<ReminderListModel> list, final Prescr_Details_Adapter.ViewHolder holder, final int position){
        if(optionsPickerView == null) {
            optionsPickerView = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    holder.dose_text.setText("一次"+dosageList[options1]+dosageUnitList[options2]);
                    list.get(position).setDosage(options1+1+"");
                    list.get(position).setDosageUnit(options2+1+"");
                }
            }).setSubmitText("确定")//确定按钮文字
                    .setCancelText("取消")//取消按钮文字
                    .setTitleText("类型选择")//标题
                    .setSubCalSize(18)//确定和取消文字大小
                    .setTitleSize(20)//标题文字大小
                    .setTitleColor(Color.BLACK)//标题文字颜色
                    .setSubmitColor(context.getResources().getColor(R.color.main_color))//确定按钮文字颜色
                    .setCancelColor(context.getResources().getColor(R.color.main_color))//取消按钮文字颜色
                    .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                    .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                    .setDividerColor(context.getResources().getColor(R.color.title_bottom_line))
                    .setTextColorCenter(context.getResources().getColor(R.color.main_color))//设置选中项的颜色
                    .setTextColorOut(context.getResources().getColor(R.color.line_btn))//设置没有被选中项的颜色
                    .setContentTextSize(18)//滚轮文字大小
                    .setLinkage(false)//设置是否联动，默认true
//                .setLabels("", "","")//设置选择的三级单位
                    .setCyclic(false, false, false)//循环与否
                    .setSelectOptions(1, 1)  //设置默认选中项
                    .setOutSideCancelable(false)//点击外部dismiss default true
                    .build();

            optionsPickerView.setNPicker(Arrays.asList(context.getResources().getStringArray(R.array.dosage)),
                    Arrays.asList(context.getResources().getStringArray(R.array.dosageUnit)),null);
//            optionsPickerView.setPicker(list.get(position).getInfoAry().get(7).getPickerAry().get(0),
//                    list.get(position).getInfoAry().get(7).getPickerAry());
            optionsPickerView.show();
        }else{
            optionsPickerView.show();
        }
    }
}
