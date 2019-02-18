//package zj.health.health_v1.Adapter;
//
//import android.content.Context;
//import android.content.Intent;
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
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
////import zj.health.health_v1.Activity.Exercise_schedule_Activity;
////import zj.health.health_v1.Activity.Measurement_schedule_Activity;
//import zj.health.health_v1.Activity.Take_Measurement_Week_Activity;
//import zj.health.health_v1.Implements.OnItemClick;
//import zj.health.health_v1.Model.CalendarSportNotice;
//import zj.health.health_v1.MyView.CreateDialog;
//import zj.health.health_v1.R;
//import zj.health.health_v1.Utils.DateUtil;
//import zj.health.health_v1.Utils.StringUtil;
//
///**
// * Created by Administrator on 2018/6/22.
// */
//
//public class Take_Sport_Adapter  extends RecyclerView.Adapter<Take_Sport_Adapter.ViewHolder>{
//
//    private Context context;
//    private List<CalendarSportNotice> list;
//    private OnItemClick onItemClick;
//    private List<String> deleteIdlist = new ArrayList<>();
//    private String selectDay = null;
//
//
//    public Take_Sport_Adapter(Context context,List<CalendarSportNotice> list,String selectDay){
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
//    public List<CalendarSportNotice> getList(){
//        return list;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.make_sport_layout,parent,false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, final int position) {
//
//        if(position >0){
//            holder.delete_rela.setVisibility(View.VISIBLE);
//        }
//
//        //设置运动类型名称数据
//        List<String> sportTypeList = Arrays.asList(context.getResources().getStringArray(R.array.sport_array));
//        if(!StringUtil.isEmpty(list.get(0).getId())) {
//            if (StringUtil.isEmpty(list.get(position).getInfoAry().get(0).getrText())) {
//                holder.sport_type_text.setText(sportTypeList.get(0));
//            } else {
//                holder.sport_type_text.setText(list.get(position).getInfoAry().get(0).getrText());
//            }
//        }else{
//            holder.sport_type_text.setText(sportTypeList.get(0));
//        }
//
//
//        //设置运动方式名称数据
//        List<String> sportModeList = Arrays.asList(context.getResources().getStringArray(R.array.sport_frequency));
//        if(!StringUtil.isEmpty(list.get(0).getId())) {
//            if (list.get(position).getInfoAry().get(3).getIsShow()) {
//                holder.sport_mode_text.setText(list.get(position).getInfoAry().get(3).getrText());
//            } else {
//                holder.sport_mode_text.setText(list.get(position).getInfoAry().get(4).getrText());
//            }
//        }else{
//            holder.sport_mode_text.setText(sportModeList.get(0));
//        }
//
//        //设置结束时间数据
//        if(StringUtil.isEmpty(list.get(position).getInfoAry().get(6).getrText()) || list.get(position).getInfoAry().get(6).getrText().equals("")){
//            holder.endDate_text.setText(selectDay);
//            list.get(position).getInfoAry().get(6).setrText(selectDay);
//        }else {
//            holder.endDate_text.setText(list.get(position).getInfoAry().get(6).getrText());
//        }
//
//        //设置备注数据
//        if(!StringUtil.isEmpty(list.get(position).getInfoAry().get(1).getrText())){
//            holder.Remarks_text.setText(list.get(position).getInfoAry().get(1).getrText());
//        }
//
//
//
//        holder.startDate_text.setText(list.get(position).getInfoAry().get(5).getrText());
//
//        final CreateDialog dialog = new CreateDialog();
//        final OptionsPickerView optionsPickerView = null;
//
//
//        //运动类型点击监听
//        final View.OnClickListener sportType_ClickListener = new View.OnClickListener() {
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
//                    case R.id.cancel_rela:
//                        dialog.getPopupWindow().dismiss();
//                        break;
//
//                }
//            }
//        };
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
//        holder.sport_type_rela.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.Sport_Type_Dailog(context,sportType_ClickListener,
//                        android.R.style.Animation_Toast,list.get(position).getInfoAry().get(0).getItemAry(),position);
//            }
//        });
//
//        //测量项目点击监听
//        final View.OnClickListener sportMode_ClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (view.getId()){
//                    case R.id.day_one_rela:
////                        List<CalendarNoticeModel.infoAry> infoAries
//                        list.get(position).getInfoAry().get(3).
//                                setrText(list.get(position).getInfoAry().get(3).getPickerAry().get(0).get(0));
//                        dialog.getPopupWindow().dismiss();
//                        notifyDataSetChanged();
//                        break;
//                    case R.id.day_two_rela:
//                        list.get(position).getInfoAry().get(3).
//                                setrText(list.get(position).getInfoAry().get(3).getPickerAry().get(0).get(1));
//                        dialog.getPopupWindow().dismiss();
//                        notifyDataSetChanged();
//                        break;
//                    case R.id.day_three_rela:
//                        list.get(position).getInfoAry().get(3).
//                                setrText(list.get(position).getInfoAry().get(3).getPickerAry().get(0).get(2));
//                        dialog.getPopupWindow().dismiss();
//                        notifyDataSetChanged();
//                        break;
//                    case R.id.day_four_rela:
//                        list.get(position).getInfoAry().get(3).
//                                setrText(list.get(position).getInfoAry().get(3).getPickerAry().get(0).get(3));
//                        dialog.getPopupWindow().dismiss();
//                        notifyDataSetChanged();
//                        break;
//                    case R.id.day_five_rela:
//                        list.get(position).getInfoAry().get(3).
//                                setrText(list.get(position).getInfoAry().get(3).getPickerAry().get(0).get(4));
//                        dialog.getPopupWindow().dismiss();
//                        notifyDataSetChanged();
//                        break;
//
//                    case R.id.cancel_rela:
//
//                        dialog.getPopupWindow().dismiss();
//                        break;
//
//
//                }
//            }
//        };
//
//        holder.sport_mode_rela.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(list.get(position).getInfoAry().get(3).getIsShow()){
//                    dialog.SportMode_Dailog(context,sportMode_ClickListener,
//                            android.R.style.Animation_Toast,list.get(position).getInfoAry().get(3).getPickerAry().get(0),position);
//                }else{
//                    Intent it = new Intent(context, Take_Measurement_Week_Activity.class);
//                    it.putExtra("position",position);
//                    it.putExtra("type",1);
//                    it.putExtra("name",list.get(position).getInfoAry().get(4).getrText());
//                    it.putExtra("repeat",list.get(position).getInfoAry().get(4).getRepeat());
//                    ((Exercise_schedule_Activity)context).startActivityForResult(it,0x3);
//                }
//
//            }
//        });
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
//
//
//        if(list.get(position).getInfoAry().get(2).getRadioAry().get(0).isChecked()){
//            holder.circle_img1.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_tick));
//            holder.circle_img2.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_hollow));
//        }else{
//            holder.circle_img2.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_tick));
//            holder.circle_img1.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_hollow));
//        }
//
//
//        holder.circle_img1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.circle_img1.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_tick));
//                holder.circle_img2.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_hollow));
//                list.get(position).getInfoAry().get(2).getRadioAry().get(0).setChecked(true);
//                list.get(position).getInfoAry().get(2).getRadioAry().get(1).setChecked(false);
//                list.get(position).getInfoAry().get(3).setIsShow(true);
//                list.get(position).getInfoAry().get(4).setIsShow(false);
//                holder.sport_mode_text.setText(list.get(position).getInfoAry().get(3).getrText());
//
//
//            }
//        });
//        holder.circle_img2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.circle_img2.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_tick));
//                holder.circle_img1.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_hollow));
//                list.get(position).getInfoAry().get(2).getRadioAry().get(0).setChecked(false);
//                list.get(position).getInfoAry().get(2).getRadioAry().get(1).setChecked(true);
//                list.get(position).getInfoAry().get(3).setIsShow(false);
//                list.get(position).getInfoAry().get(4).setIsShow(true);
//                holder.sport_mode_text.setText(list.get(position).getInfoAry().get(4).getrText());
//            }
//        });
//
//
//
//        final TimePickerView.OnTimeSelectListener listener = new TimePickerView.OnTimeSelectListener(){
//
//            @Override
//            public void onTimeSelect(Date date, View v) {
//
//                if(DateUtil.compareDateYmd(DateUtil.getNowdayymd(),DateUtil.getDateStrYmd(date))){
//                    holder.endDate_text.setText(DateUtil.getDateStrYmd(date));
//                    list.get(position).getInfoAry().get(6).setrText(DateUtil.getDateStrYmd(date));
//                }else{
//                    Toast.makeText(context, context.getString(R.string.please_arrange_the_time), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        };
//
//        final TimePickerView.OnTimeSelectListener Startlistener = new TimePickerView.OnTimeSelectListener(){
//
//            @Override
//            public void onTimeSelect(Date date, View v) {
//
//                    holder.startDate_text.setText(DateUtil.getDaysStrhm(date));
//                    list.get(position).getInfoAry().get(5).setrText(DateUtil.getDaysStrhm(date));
//
//            }
//        };
//
//        final TimePickerView timePickerView = new DateUtil().ShowTimePickerView(context,listener, Calendar.getInstance(),
//                true,true,true,false,false,false);
//
//        final TimePickerView timeStartPickerView = new DateUtil().ShowTimePickerView(context,Startlistener, Calendar.getInstance(),
//                false,false,false,true,true,false);
//
//        holder.startDate_rela.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                timeStartPickerView.show();
//            }
//        });
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
//    public void setViewList(List<CalendarSportNotice> list){
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
//        RelativeLayout delete_rela,sport_type_rela,sport_mode_rela,endDate_rela,startDate_rela;
//        TextView sport_type_text,sport_mode_text,
//                startDate_text,endDate_text,Remarks_text;
//        Button delete_Button;
//        ImageView circle_img1,circle_img2;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            delete_rela = (RelativeLayout)itemView.findViewById(R.id.delete_rela);
//            delete_Button =(Button)itemView.findViewById(R.id.delete_Button);
//            sport_type_rela = (RelativeLayout)itemView.findViewById(R.id.sport_type_rela);
//            sport_mode_rela = (RelativeLayout)itemView.findViewById(R.id.sport_mode_rela);
//            endDate_rela = (RelativeLayout)itemView.findViewById(R.id.endDate_rela);
//            startDate_rela = (RelativeLayout)itemView.findViewById(R.id.startDate_rela);
//            sport_type_text = (TextView)itemView.findViewById(R.id.sport_type_text);
//            sport_mode_text = (TextView)itemView.findViewById(R.id.sport_mode_text);
//            startDate_text = (TextView)itemView.findViewById(R.id.startDate_text);
//            endDate_text = (TextView)itemView.findViewById(R.id.endDate_text);
//            Remarks_text = (EditText)itemView.findViewById(R.id.Remarks_text);
//            circle_img1 = (ImageView)itemView.findViewById(R.id.circle_img1);
//            circle_img2 = (ImageView)itemView.findViewById(R.id.circle_img2);
//
//        }
//    }
//
//
//}
