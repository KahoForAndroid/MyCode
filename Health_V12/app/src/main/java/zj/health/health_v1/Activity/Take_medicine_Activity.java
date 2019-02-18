package zj.health.health_v1.Activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.Take_medicine_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.Medicine_DialogOnitemClick;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.CalendarNoticeModel;
import zj.health.health_v1.Model.RemindCalendarModel;
import zj.health.health_v1.Model.ReminderListModel;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Service.AlarmManageService;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.HttpUtil;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * 服药提醒页面
 * Created by Administrator on 2018/6/13.
 */

public class Take_medicine_Activity extends BaseActivity implements View.OnClickListener{

    private RecyclerView layout_recy;
    private Take_medicine_Adapter adapter;
    private ImageView back;
    private TextView title;
    private Button add_Button,save_Button;
    private List<ReminderListModel> list = new ArrayList<>();
    private Bundle bundle = new Bundle();
    private final int DRUGNAME_RESUTL = 0x112;
    private final int MEDICINE_REPEAT = 0x113;
    private int deleteSize = 0;
    private Gson gson = new Gson();
    private String selectDay = null;
    private DateUtil dateUtil = new DateUtil();
    private int DeletePosition;
    private boolean updated;//是否需要刷新日历页面


    private Handler reminderListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        list.clear();
                        Health_AppLocation.instance.reminderListModelList = gson.fromJson(jsonObject.optString("data"),new TypeToken<List<ReminderListModel>>(){}.getType());
                        if(Health_AppLocation.instance.reminderListModelList.size()>0){
                            Date nowDate = DateUtil.str2Dates2(selectDay+" "+"00:00:01");

                            for (int i = 0;i<Health_AppLocation.instance.reminderListModelList.size();i++){
                                try {
                                String startString = DateUtil.UTCToCST(Health_AppLocation.instance.reminderListModelList.get(i).getStartTime());
                                Health_AppLocation.instance.reminderListModelList.get(i).setStartTime(DateUtil.dateToStamp(startString+":00"));
                                Date startDate = DateUtil.str2Dates2(startString+":00");

                                String endString =  DateUtil.UTCToCST(Health_AppLocation.instance.reminderListModelList.get(i).getEndTime());
                                Health_AppLocation.instance.reminderListModelList.get(i).setEndTime(DateUtil.dateToStamp(endString+":00"));
                                Date endDate = DateUtil.str2Dates2(endString+":00");

                                    boolean isTimes = false;
                                if(DateUtil.isSameDate(nowDate,startDate) || (startDate.getTime()<nowDate.getTime() && endDate.getTime()>nowDate.getTime())){
                                    isTimes = true;
                                }
//                                boolean isTimes = dateUtil.belongCalendar(nowDate,startDate,endDate);
                                if(isTimes){
                                    list.add(Health_AppLocation.instance.reminderListModelList.get(i));
                                }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(list.size()<=0){
                                ReminderListModel reminderListModel = new ReminderListModel();
                                list.add(reminderListModel);

                            }

                        }else{
                            ReminderListModel reminderListModel = new ReminderListModel();
                            list.add(reminderListModel);
                        }
                        if(Long.parseLong(DateUtil.dateToStamp(selectDay+" "+"00:00:00"))<Long.parseLong(DateUtil.dateToStamp(DateUtil.getNowdayymd()+" "+"00:00:00"))){
                            add_Button.setVisibility(View.GONE);
                            save_Button.setVisibility(View.GONE);
                            adapter.setViewList(list,false);

                        }else{
                            add_Button.setVisibility(View.VISIBLE);
                            save_Button.setVisibility(View.VISIBLE);
                            adapter.setViewList(list,true);
                        }
                    }else{
                        Toast.makeText(Take_medicine_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Take_medicine_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Take_medicine_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Take_medicine_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };


    private Handler CommitreminderHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        Intent it = new Intent(Take_medicine_Activity.this, AlarmManageService.class);
                        startService(it);
                        Toast.makeText(Take_medicine_Activity.this, R.string.commit_success, Toast.LENGTH_SHORT).show();
                        setResult(0x113);
                        finish();
                    }else{
                           Toast.makeText(Take_medicine_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Take_medicine_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Take_medicine_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Take_medicine_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler DeleteMedicineHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
//                        Thread.sleep(1000);
                        updated = true;
                        list.remove(DeletePosition);
                        if(list.size()<=0){
                            setResult(0x113);
                            finish();
                        }else{
                            getReminderList();
                        }
                    }else{
                        Toast.makeText(Take_medicine_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Take_medicine_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Take_medicine_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Take_medicine_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_medicine_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        layout_recy = (RecyclerView)findViewById(R.id.layout_recy);
        add_Button = (Button)findViewById(R.id.add_Button);
        save_Button = (Button)findViewById(R.id.save_Button);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        add_Button.setOnClickListener(this);
        save_Button.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.prescription_remind));

        selectDay = getIntent().getStringExtra("selectDay");
//        if(Health_AppLocation.instance.reminderListModelList.size()>0){
//            Date nowDate = new Date(System.currentTimeMillis());
//
//            for (int i = 0;i<Health_AppLocation.instance.reminderListModelList.size();i++){
//
//                Date startDate = null;
//                Date endDate = null;
//                try {
//                    startDate = DateUtil.str2Dates2(DateUtil.dateToStamp(Health_AppLocation.instance.reminderListModelList.get(i).getStartTime()));
//                    endDate = DateUtil.str2Dates2(DateUtil.dateToStamp(Health_AppLocation.instance.reminderListModelList.get(i).getEndTime()));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                boolean isTimes = dateUtil.belongCalendar(nowDate,startDate,endDate);
//                if(isTimes){
//                    list.add(Health_AppLocation.instance.reminderListModelList.get(i));
//                }
//            }
//        }else{
//            getReminderList();
//        }
        getReminderList();
        adapter = new Take_medicine_Adapter(Take_medicine_Activity.this,list,selectDay);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                DeletePosition = position;
                DeleteReminder(list.get(position).getId());
            }
        });

        layout_recy.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        layout_recy.setAdapter(adapter);

    }


    private void getReminderList(){
        new PostUtils().Get(Constant.reminderList,true,reminderListHandler,this);
    }
    private void DeleteReminder(String id){
        String json = "reminderId="+id;
        new PostUtils().Delete(Constant.DeleteMedicine+json,true,DeleteMedicineHandler,this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                if(updated){
                    setResult(0x113);
                }
                finish();
                break;
            case R.id.add_Button:
                list.add(new ReminderListModel());
                adapter.setViewList(list,true);
                adapter.notifyDataSetChanged();
                break;
            case R.id.save_Button:
                boolean isCommit = false;
                for (int i = 0;i<list.size();i++){
                    if(StringUtil.isEmpty(list.get(i).getMedicineName())){
                        isCommit = false;
                        Toast.makeText(this, R.string.medicineName_null_error, Toast.LENGTH_SHORT).show();
                        break;
                    }else if(StringUtil.isEmpty(list.get(i).getTimesOneDay())){
                        isCommit = false;
                        Toast.makeText(this, R.string.timesOneDay_null_error, Toast.LENGTH_SHORT).show();
                        break;
                    }else if(StringUtil.isEmpty(list.get(i).getDosage())){
                        isCommit = false;
                        Toast.makeText(this, R.string.dosage_null_error, Toast.LENGTH_SHORT).show();
                        break;
                    }else if(StringUtil.isEmpty(list.get(i).getTreatment())){
                        isCommit = false;
                        Toast.makeText(this, R.string.treatment_null_error, Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        isCommit = true;
                    }
                }

                if(isCommit){
                    String json = gson.toJson(adapter.getlist());
                    Commit(json);
                }
                break;
                default:
                    break;
        }
    }

       private void Commit(String json){
        new PostUtils().JsonPost(Constant.commitRemind,json,CommitreminderHandler,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //填写药物名返回
        if(resultCode == DRUGNAME_RESUTL){
            int position = data.getIntExtra("position",0);
            String name = data.getStringExtra("name");
            list.get(position).setMedicineName(name);
            adapter.setViewList(list,true);
        }else if(resultCode == MEDICINE_REPEAT){
            Calendar calendar = Calendar.getInstance();

            int position = data.getIntExtra("position",0);
            String name = data.getStringExtra("name");
            list.get(position).setTreatment(name);
            list.get(position).setIntervalMode(1+"");
            list.get(position).setIntervalTemplate(1+"");
//            list.get(position).setStartTime(calendar.getTimeInMillis()+"");
            if(name.equals("1")){
                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)+1,23,59);
            }else if(name.equals("2")){
                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)+3,23,59);
            }else if(name.equals("3")){
                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)+7,23,59);
            }else{
                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+2,calendar.get(Calendar.DAY_OF_MONTH),23,59);
            }
            list.get(position).setEndTime(calendar.getTimeInMillis()+"");
            adapter.setViewList(list,true);
        }
    }

    @Override
    public void onBackPressed() {
        if(updated){
            setResult(0x111);
        }
        finish();
    }
}
