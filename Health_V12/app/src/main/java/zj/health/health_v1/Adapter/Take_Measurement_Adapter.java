//package zj.health.health_v1.Adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bigkoo.pickerview.OptionsPickerView;
//import com.bigkoo.pickerview.TimePickerView;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import zj.health.health_v1.Activity.Add_drug_for_calendar_Activity;
//import zj.health.health_v1.Activity.Calendar_Take_medicine_repeat_Activity;
////import zj.health.health_v1.Activity.Measurement_schedule_Activity;
//import zj.health.health_v1.Activity.Take_Measurement_Week_Activity;
//import zj.health.health_v1.Activity.Take_medicine_Activity;
//import zj.health.health_v1.Implements.OnItemClick;
//import zj.health.health_v1.Model.CalendarMeasureNotice;
//import zj.health.health_v1.Model.CalendarNoticeModel;
//import zj.health.health_v1.Model.CalendarSportNotice;
//import zj.health.health_v1.MyView.CreateDialog;
//import zj.health.health_v1.R;
//import zj.health.health_v1.Utils.DateUtil;
//import zj.health.health_v1.Utils.StringUtil;
//
///**
// * Created by Administrator on 2018/6/21.
// */
//
//public class Take_Measurement_Adapter extends RecyclerView.Adapter<Take_Measurement_Adapter.ViewHolder>{
//
//    private Context context;
//    private List<CalendarMeasureNotice> list;
//    private OnItemClick onItemClick;
//    private List<String> deleteIdlist = new ArrayList<>();
//    private String selectDay = null;
//
//
//    public Take_Measurement_Adapter(Context context,List<CalendarMeasureNotice> list,String selectDay){
//        this.context = context;
//        this.list = list;
//        this.selectDay = selectDay;
//    }
//
//    public void setOnItemClick(OnItemClick onItemClick){
//        this.onItemClick = onItemClick;
//    }
//
//    public List<String> getDeleteId(){
//        return deleteIdlist;
//    }
//
//    public List<CalendarMeasureNotice> getList(){
//        return list;
//    }
//
//
//    @Override
//    public Take_Measurement_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.make_measurement_layout,parent,false);
//        Take_Measurement_Adapter.ViewHolder viewHolder = new Take_Measurement_Adapter.ViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final Take_Measurement_Adapter.ViewHolder holder, final int position) {
//
//        if(position >0){
//            holder.delete_rela.setVisibility(View.VISIBLE);
//        }
//
//        //设置测量项目名称数据
//        if(!StringUtil.isEmpty(list.get(0).getId())) {
//            if (StringUtil.isEmpty(list.get(position).getInfoAry().get(0).getrText())) {
//                holder.measurement_type_text.setText(context.getString(R.string.please_input));
//            } else {
//                holder.measurement_type_text.setText(list.get(position).getInfoAry().get(0).getrText());
//            }
//        }else{
//            holder.measurement_type_text.setText(context.getString(R.string.please_input));
//        }
//
//        //设置结束时间数据
//        if(StringUtil.isEmpty(list.get(position).getInfoAry().get(7).getrText()) || list.get(position).getInfoAry().get(7).getrText().equals("")){
//            holder.endDate_text.setText(selectDay);
//            list.get(position).getInfoAry().get(7).setrText(selectDay);
//        }else {
//            holder.endDate_text.setText(list.get(position).getInfoAry().get(7).getrText());
//        }
//
//        //设置备注数据
//        if(!StringUtil.isEmpty(list.get(position).getInfoAry().get(1).getrText())){
//            holder.Remarks_text.setText(list.get(position).getInfoAry().get(1).getrText());
//        }
//
//        holder.measurement_much_text.setText(list.get(position).getInfoAry().get(2).getrText());
//        holder.Schedule_text.setText(list.get(position).getInfoAry().get(6).getrText());
//
//        final CreateDialog dialog = new CreateDialog();
//        final OptionsPickerView optionsPickerView = null;
//        final Take_MeasureTime_Adapter take_measureTime_adapter = new Take_MeasureTime_Adapter(context,list,position);
//        holder.much_recy.setLayoutManager(new LinearLayoutManager(context));
//        holder.much_recy.setAdapter(take_measureTime_adapter);
//
//
//        holder.Remarks_text.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                list.get(position).getInfoAry().get(1).setrText(holder.Remarks_text.getText().toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//
//        //测量次数点击监听
//        final View.OnClickListener measurement_much_ClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (view.getId()){
//                    case R.id.day_one_rela:
////                        List<CalendarNoticeModel.infoAry> infoAries
//                        list.get(position).getInfoAry().get(2).setrText(list.get(position).getInfoAry().get(2).getItemAry().get(0));
//                        dialog.getPopupWindow().dismiss();
//                        for (int i =3;i<6;i++){
//                            if(i == 3){
//                                list.get(position).getInfoAry().get(i).setIsShow(true);
//                            }else{
//                                list.get(position).getInfoAry().get(i).setIsShow(false);
//                            }
//                        }
//                        take_measureTime_adapter.setList(list);
//                        take_measureTime_adapter.notifyDataSetChanged();
//                        notifyDataSetChanged();
//                        break;
//                    case R.id.day_two_rela:
//                        list.get(position).getInfoAry().get(2).setrText(list.get(position).getInfoAry().get(2).getItemAry().get(1));
//                        dialog.getPopupWindow().dismiss();
//
//                        for (int i =3;i<6;i++){
//                            if(i <=4){
//                                list.get(position).getInfoAry().get(i).setIsShow(true);
//                            }else{
//                                list.get(position).getInfoAry().get(i).setIsShow(false);
//                            }
//                        }
//                        take_measureTime_adapter.setList(list);
//                        take_measureTime_adapter.notifyDataSetChanged();
//                        notifyDataSetChanged();
//                        break;
//                    case R.id.day_three_rela:
//                        list.get(position).getInfoAry().get(2).setrText(list.get(position).getInfoAry().get(2).getItemAry().get(2));
//                        dialog.getPopupWindow().dismiss();
//                        for (int i =3;i<6;i++){
//                            if(i <=5){
//                                list.get(position).getInfoAry().get(i).setIsShow(true);
//                            }else{
//                                list.get(position).getInfoAry().get(i).setIsShow(false);
//                            }
//                        }
//                        take_measureTime_adapter.setList(list);
//                        take_measureTime_adapter.notifyDataSetChanged();
//                        notifyDataSetChanged();
//                        break;
//
//                }
//            }
//        };
//
//
//        //测量项目点击监听
//        final View.OnClickListener measurement_type_ClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (view.getId()){
//                    case R.id.day_one_rela:
////                        List<CalendarNoticeModel.infoAry> infoAries
//                        list.get(position).getInfoAry().get(0).setrText(list.get(position).getInfoAry().get(0).getItemAry().get(0));
//                        dialog.getPopupWindow().dismiss();
//                        notifyDataSetChanged();
//                        break;
//                    case R.id.day_two_rela:
//                        list.get(position).getInfoAry().get(0).setrText(list.get(position).getInfoAry().get(0).getItemAry().get(1));
//                        dialog.getPopupWindow().dismiss();
//                        notifyDataSetChanged();
//                        break;
//                    case R.id.day_three_rela:
//                        list.get(position).getInfoAry().get(0).setrText(list.get(position).getInfoAry().get(0).getItemAry().get(2));
//                        dialog.getPopupWindow().dismiss();
//                        notifyDataSetChanged();
//                        break;
//                    case R.id.day_four_rela:
//                        list.get(position).getInfoAry().get(0).setrText(list.get(position).getInfoAry().get(0).getItemAry().get(3));
//                        dialog.getPopupWindow().dismiss();
//                        notifyDataSetChanged();
//                        break;
//
//                }
//            }
//        };
//        holder.delete_Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!StringUtil.isEmpty(list.get(position).getId()) && !list.get(position).getId().equals("0")){
//                    deleteIdlist.add(list.get(position).getId());
//                }
//                list.remove(position);
//                Toast.makeText(context, context.getString(R.string.medicine_delete_tips), Toast.LENGTH_SHORT).show();
//                notifyDataSetChanged();
//            }
//        });
//        holder.measurement_type_rela.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<String> much_list = list.get(position).getInfoAry().get(0).getItemAry();
//                dialog.Measurement_Type_Dailog(context, measurement_type_ClickListener,
//                        android.R.style.Animation_Toast,much_list,position);
//            }
//        });
//        holder.measurement_much_rela.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<String> much_list = list.get(position).getInfoAry().get(2).getItemAry();
//                dialog.Measurement_much_Dailog(context, measurement_much_ClickListener,
//                        android.R.style.Animation_Toast,much_list,position);
//            }
//        });
//
//        holder.Schedule_rela.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent it = new Intent(context, Take_Measurement_Week_Activity.class);
//                it.putExtra("position",position);
//                it.putExtra("type",1);
//                it.putExtra("name",list.get(position).getInfoAry().get(6).getrText());
//                it.putExtra("repeat",list.get(position).getInfoAry().get(6).getRepeat());
//                ((Measurement_schedule_Activity)context).startActivityForResult(it,0x3);
//            }
//        });
//
//        final TimePickerView.OnTimeSelectListener listener = new TimePickerView.OnTimeSelectListener(){
//
//            @Override
//            public void onTimeSelect(Date date, View v) {
//
//                if(DateUtil.compareDateYmd(DateUtil.getNowdayymd(),DateUtil.getDateStrYmd(date))){
//                    holder.endDate_text.setText(DateUtil.getDateStrYmd(date));
//                    list.get(position).getInfoAry().get(7).setrText(DateUtil.getDateStrYmd(date));
//                }else{
//                    Toast.makeText(context, context.getString(R.string.please_arrange_the_time), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        };
//
//        final TimePickerView timePickerView = new DateUtil().ShowTimePickerView(context,listener, Calendar.getInstance(),
//                true,true,true,false,false,false);
//
//        holder.endDate_rela.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                timePickerView.show();
//            }
//        });
//    }
//
//    public void setViewList(List<CalendarMeasureNotice> list){
//        this.list = list;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder{
//
//        RelativeLayout delete_rela,measurement_type_rela,measurement_much_rela,endDate_rela,Schedule_rela;
//        RecyclerView much_recy;
//        TextView measurement_type_text,measurement_much_text,
//                Schedule_text,endDate_text,Remarks_text;
//        Button delete_Button;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            delete_rela = (RelativeLayout)itemView.findViewById(R.id.delete_rela);
//            delete_Button =(Button)itemView.findViewById(R.id.delete_Button);
//            measurement_type_rela = (RelativeLayout)itemView.findViewById(R.id.measurement_type_rela);
//            measurement_much_rela = (RelativeLayout)itemView.findViewById(R.id.measurement_much_rela);
//            endDate_rela = (RelativeLayout)itemView.findViewById(R.id.endDate_rela);
//            Schedule_rela = (RelativeLayout)itemView.findViewById(R.id.Schedule_rela);
//            much_recy = (RecyclerView)itemView.findViewById(R.id.much_recy);
//            measurement_type_text = (TextView)itemView.findViewById(R.id.measurement_type_text);
//            measurement_much_text = (TextView)itemView.findViewById(R.id.measurement_much_text);
//            Schedule_text = (TextView)itemView.findViewById(R.id.Schedule_text);
//            endDate_text = (TextView)itemView.findViewById(R.id.endDate_text);
//            Remarks_text = (EditText)itemView.findViewById(R.id.Remarks_text);
//        }
//    }
//
//
//}
//
