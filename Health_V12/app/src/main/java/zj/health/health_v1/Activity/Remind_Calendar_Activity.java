package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.CalendarList_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.CalendarDateListModel;
import zj.health.health_v1.Model.CalendarMeasureNotice;
import zj.health.health_v1.Model.CalendarNoticeListModel;
import zj.health.health_v1.Model.CalendarNoticeModel;
import zj.health.health_v1.Model.CalendarSportNotice;
import zj.health.health_v1.Model.RemindCalendarModel;
import zj.health.health_v1.MyView.CalendarView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/8.
 */

public class Remind_Calendar_Activity extends BaseActivity implements View.OnClickListener{

    private CalendarView calendar;
    private RelativeLayout take_medicine_rela,measurement_schedule_rela,exercise_schedule_rela;
    private ImageView back;
    private TextView title;
    private List<CalendarDateListModel> list;
    private List<CalendarNoticeModel> calendarNoticeModels = new ArrayList<>();
    private List<CalendarSportNotice> calendarSportNotices = new ArrayList<>();
    private List<CalendarMeasureNotice> calendarMeasureNotices = new ArrayList<>();
    private List<CalendarNoticeListModel> listModels = new ArrayList<>();

    private List<RemindCalendarModel> remindCalendarModelList = new ArrayList<>();
    private Gson gson = new Gson();
    private RecyclerView calendar_recy;
    private LinearLayout take_medicine_lin;
    private CalendarList_Adapter adapter;
    private Intent it;
    private String selectDay = null;
    private Handler getListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        remindCalendarModelList = gson.fromJson(jsonObject.optString("data"),new TypeToken<List<RemindCalendarModel>>(){}.getType());
                        calendar.setList(remindCalendarModelList);
                        List<RemindCalendarModel> list = new ArrayList<>();
                        for (int i = 0;i<remindCalendarModelList.size();i++){
                            for (int j = 0;j<remindCalendarModelList.get(i).getReminderDaily().size();j++){
                                if(DateUtil.getDateStrYmd(new Date(Long.parseLong(remindCalendarModelList.get(i).getReminderDaily().get(j).getEventTime()))).
                                        equals(selectDay)){
                                    list.add(remindCalendarModelList.get(i));
                                }
                            }

                        }
                        adapter.setList(list,selectDay);
                    }else{
                        Toast.makeText(Remind_Calendar_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Remind_Calendar_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Remind_Calendar_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Remind_Calendar_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler TakeDrugHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        getCalendarDate(System.currentTimeMillis()+"");
                    }else{
                        Toast.makeText(Remind_Calendar_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Remind_Calendar_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Remind_Calendar_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Remind_Calendar_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind_calendar_activity);
        initView();
        BindListener();
        setData();
    }


    private void initView(){
        calendar = (CalendarView)findViewById(R.id.calendar);
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        calendar_recy = (RecyclerView)findViewById(R.id.calendar_recy);
        take_medicine_rela = (RelativeLayout)findViewById(R.id.take_medicine_rela);
        take_medicine_lin = (LinearLayout)findViewById(R.id.take_medicine_lin);
//        measurement_schedule_rela = (RelativeLayout)findViewById(R.id.measurement_schedule_rela);
//    exercise_schedule_rela = (RelativeLayout)findViewById(R.id.exercise_schedule_rela);
}

    private void BindListener(){
        take_medicine_rela.setOnClickListener(this);
//        measurement_schedule_rela.setOnClickListener(this);
//        exercise_schedule_rela.setOnClickListener(this);
        back.setOnClickListener(this);
        calendar.setOnClickListener(new CalendarView.onClickListener() {
            @Override
            public void onLeftRowClick() {
                calendar.getList().clear();
                calendar.monthChange(-1);
                getCalendarDate(calendar.getMonth()+"");
            }

            @Override
            public void onRightRowClick() {
                calendar.getList().clear();
                calendar.monthChange(1);
                getCalendarDate(calendar.getMonth()+"");
            }

            @Override
            public void onTitleClick(String monthStr, Date month) {

            }

            @Override
            public void onWeekClick(int weekIndex, String weekStr) {

            }

            @Override
            public void onDayClick(int day, String dayStr) {
                selectDay = DateUtil.getDateStrYmd(DateUtil.str2Dates(dayStr));
//                getCalendarDateToList(selectDay);
                List<RemindCalendarModel> list = new ArrayList<>();
                for (int i = 0;i<remindCalendarModelList.size();i++){
                    for (int j = 0;j<remindCalendarModelList.get(i).getReminderDaily().size();j++){
//                        String remindDay = DateUtil.getDateStrYmd(new Date(Long.parseLong(remindCalendarModelList.get(i).getEventTimes().get(j))));
//                        String nowDay = DateUtil.getNowdayymd();
                        if(DateUtil.getDateStrYmd(new Date(Long.parseLong(remindCalendarModelList.get(i).getReminderDaily().get(j).getEventTime()))).
                                equals(selectDay)){
                            list.add(remindCalendarModelList.get(i));
                        }
                    }

                }
                adapter.setList(list,selectDay);
            }
        });

    }

    private void setData(){
        title.setText(getString(R.string.remind));
        selectDay = calendar.getDateStr();
        //是否从亲友日志进入
        if(getIntent().getStringExtra("friendid")!=null){
            take_medicine_lin.setVisibility(View.GONE);
            adapter = new CalendarList_Adapter(this,remindCalendarModelList,selectDay,true);
        }else{
            take_medicine_lin.setVisibility(View.VISIBLE);
            adapter = new CalendarList_Adapter(this,remindCalendarModelList,selectDay,false);
        }

        calendar_recy.setLayoutManager(new LinearLayoutManager(this));
        calendar_recy.setAdapter(adapter);
        getCalendarDate(System.currentTimeMillis()+"");


        adapter.setOnItemClick(new CalendarList_Adapter.OnItemClick() {
            @Override
            public void itemclickListener(String reminderId, String eventTime, String reminderTime, boolean isDrug) {
                TakeDrug(reminderId,eventTime,
                        reminderTime);
            }
        });


//        adapter.setOnItemClick(new OnItemClick() {
//            @Override
//            public void OnItemClickListener(View view, int position) {
//                switch (view.getId()){
//                    case R.id.left_rela:
//                        break;
//                    case R.id.left_rela2:
//                        break;
//                    case R.id.left_rela3:
//                        break;
//                    case R.id.left_rela4:
//                        break;
//                    case R.id.title_rela:
//
//                        break;
//                        default:
//                            break;
//                }
//            }
//        });

    }

    private void getCalendarDate(String date){

        String urlQuery;
        if(getIntent().getStringExtra("friendid")!=null){
             urlQuery = "friendId="+getIntent().getStringExtra("friendid")+"&"+"date="+date;
            new PostUtils().Get(Constant.friend_reminder+urlQuery,true,getListHandler,this);
        }else{
            urlQuery = "date="+date;
            new PostUtils().Get(Constant.calendar_for_month+urlQuery,true,getListHandler,this);
        }

    }

    private void TakeDrug(String reminderId,String eventTime,String reminderTime){
        Map<String,String> map = new HashMap<>();
        map.put("reminderId",reminderId);
        map.put("eventTime",eventTime);
        map.put("reminderTime",reminderTime);
        map.put("isTake","true");
        new PostUtils().getNewPost(Constant.remind_take,map,TakeDrugHandler,this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.take_medicine_rela:
                Intent it = new Intent(Remind_Calendar_Activity.this,Take_medicine_Activity.class);
                it.putExtra("selectDay",selectDay);
                startActivityForResult(it,0x1);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0x111){
            getCalendarDate(System.currentTimeMillis()+"");
        }else if(resultCode == 0x112){
            getCalendarDate(System.currentTimeMillis()+"");
        }else if(resultCode == 0x113){
            getCalendarDate(System.currentTimeMillis()+"");
        }
    }
}
