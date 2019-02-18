//package zj.health.health_v1.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import zj.health.health_v1.Adapter.Take_Measurement_Adapter;
//import zj.health.health_v1.Adapter.Take_Sport_Adapter;
//import zj.health.health_v1.Base.BaseActivity;
//import zj.health.health_v1.Base.Health_AppLocation;
//import zj.health.health_v1.Model.CalendarMeasureNotice;
//import zj.health.health_v1.Model.CalendarNoticeModel;
//import zj.health.health_v1.Model.CalendarSportNotice;
//import zj.health.health_v1.R;
//import zj.health.health_v1.Utils.HttpUtil;
//
///**
// * 运动日程页面
// * Created by Administrator on 2018/6/13.
// */
//
//public class Exercise_schedule_Activity extends BaseActivity implements View.OnClickListener{
//
//    private RecyclerView layout_recy;
//    private Take_Sport_Adapter adapter;
//    private ImageView back;
//    private TextView title;
//    private Button add_Button,save_Button;
//    private String postName = "notification/sportSubmit?";
//    private String postDelete = "notification/notificationInfoDisable?";
//    private List<CalendarSportNotice> calendarSportNoticeList = new ArrayList<>();
//    private Bundle bundle = new Bundle();
//    private final int MEASUREMENT_REPEAT = 0x113;
//    private int deleteSize = 0;
//    private Gson gson = new Gson();
//    private String selectDay = null;
//
//    //由于数据要与小程序关联 在新建模板时 根据这个json数据进行数据封装
//    private String sportJson = " [\n" +
//            "        {\n" +
//            "            \"notificationId\": 0,\n" +
//            "            \"id\": 0,\n" +
//            "            \"type\": 0,\n" +
//            "            \"isShowBtn\": \"true\",\n" +
//            "            \"infoAry\": [\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"运动类型\",\n" +
//            "                    \"rText\": \"日常运动\",\n" +
//            "                    \"itemAry\": [\n" +
//            "                        \"日常运动\",\n" +
//            "                        \"训练\",\n" +
//            "                        \"比赛\"\n" +
//            "                    ],\n" +
//            "                    \"editType\": \"sheet\"\n" +
//            "                },\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"备注\",\n" +
//            "                    \"rText\": \"\",\n" +
//            "                    \"editType\": \"input\"\n" +
//            "                },\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"日程安排\",\n" +
//            "                    \"rText\": \"按天\",\n" +
//            "                    \"editType\": \"radio\",\n" +
//            "                    \"radioAry\": [\n" +
//            "                        {\n" +
//            "                            \"checked\": true,\n" +
//            "                            \"value\": \"按天\"\n" +
//            "                        },\n" +
//            "                        {\n" +
//            "                            \"value\": \"周\"\n" +
//            "                        }\n" +
//            "                    ]\n" +
//            "                },\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"\",\n" +
//            "                    \"rText\": \"每天\",\n" +
//            "                    \"pickerAry\": [\n" +
//            "                        [\n" +
//            "                            \"每天\",\n" +
//            "                            \"两天一次\",\n" +
//            "                            \"三天一次\",\n" +
//            "                            \"四天一次\",\n" +
//            "                            \"五天一次\"\n" +
//            "                        ]\n" +
//            "                    ],\n" +
//            "                    \"editType\": \"picker-multiSelector\",\n" +
//            "                    \"isShow\": true\n" +
//            "                },\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"\",\n" +
//            "                    \"rText\": \"星期六、日\",\n" +
//            "                    \"repeat\": \"0000011\",\n" +
//            "                    \"editType\": \"button\",\n" +
//            "                    \"url\": \"../user-defined/week?title=运动日程\",\n" +
//            "                    \"isShow\": false\n" +
//            "                },\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"开始时间\",\n" +
//            "                    \"rText\": \"16:00\",\n" +
//            "                    \"editType\": \"picker-time\"\n" +
//            "                },\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"结束日期\",\n" +
//            "                    \"rText\": \"\",\n" +
//            "                    \"editType\": \"picker-date\"\n" +
//            "                }\n" +
//            "            ]\n" +
//            "        }\n" +
//            "    ]";
//    private Handler submitHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if(msg.what == 200){
//                try {
//                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
//                    if(jsonObject.optString("msg").equals("ok")){
//
//                        if (adapter.getDeleteId().size() == 0){
//                            Toast.makeText(Exercise_schedule_Activity.this,getString(R.string.save_succ), Toast.LENGTH_SHORT).show();
//                            setResult(0x112);
//                            finish();
//                        }else{
//                            StringBuilder builder = new StringBuilder();
//                            for (int i = 0;i<adapter.getDeleteId().size();i++){
//                                if (i == 0){
//                                    builder.append(adapter.getDeleteId().get(i));
//                                }else{
//                                    builder.append(","+adapter.getDeleteId().get(i));
//                                }
//                            }
//                            PostDelete(builder.toString());
//                        }
//                    }else{
//                        Toast.makeText(Exercise_schedule_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(Exercise_schedule_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                }
//            }else{
//                Toast.makeText(Exercise_schedule_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//    private Handler DeleteHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if(msg.what == 200){
//                try {
//                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
//                    if(jsonObject.optString("msg").equals("ok")){
//
//                        Toast.makeText(Exercise_schedule_Activity.this,getString(R.string.save_succ), Toast.LENGTH_SHORT).show();
//                        setResult(0x112);
//                        finish();
//                    }else{
//                        Toast.makeText(Exercise_schedule_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(Exercise_schedule_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    };
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.make_medicine_activity);
//        initView();
//        BindListener();
//        setData();
//    }
//
//    private void initView(){
//        back = (ImageView)findViewById(R.id.back);
//        title = (TextView)findViewById(R.id.title);
//        layout_recy = (RecyclerView)findViewById(R.id.layout_recy);
//        add_Button = (Button)findViewById(R.id.add_Button);
//        save_Button = (Button)findViewById(R.id.save_Button);
//    }
//    private void BindListener(){
//        back.setOnClickListener(this);
//        add_Button.setOnClickListener(this);
//        save_Button.setOnClickListener(this);
//    }
//    private void setData(){
//        title.setText(getString(R.string.Exercise_schedule));
//        if(getIntent().getBundleExtra("bundle")!=null){
//            selectDay = getIntent().getStringExtra("date");
//            bundle = getIntent().getBundleExtra("bundle");
//            calendarSportNoticeList = (List<CalendarSportNotice>) bundle.getSerializable("list");
//        }else{
//            selectDay = getIntent().getStringExtra("date");
//            calendarSportNoticeList = gson.fromJson(sportJson,new TypeToken<List<CalendarSportNotice>>(){}.getType());
//        }
//
//        adapter = new Take_Sport_Adapter(this,calendarSportNoticeList,selectDay);
//
//        layout_recy.setLayoutManager(new LinearLayoutManager(this){
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        });
//        layout_recy.setAdapter(adapter);
//
//    }
//
//    private void PostSubmit(final String json){
////        new PostUtils().getPost(postName+json,null,submitHandler,this);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Map<String,String> map = new HashMap<>();
//                map.put("appUserId", Health_AppLocation.instance.userid);
//                map.put("info",json);
//                map.put("date",selectDay);
//                new HttpUtil().Post("http://health.zhzjzdh.com/"+postName,map,submitHandler,Exercise_schedule_Activity.this);
//            }
//        }).start();
//    }
//
//
//    private void PostDelete(final String infoId){
////        new PostUtils().getPost(postName+json,null,submitHandler,this);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Map<String,String> map = new HashMap<>();
//                map.put("appUserId",Health_AppLocation.instance.userid);
//                map.put("infoId",infoId);
//                new HttpUtil().Post("http://health.zhzjzdh.com/"+postDelete,map,DeleteHandler,Exercise_schedule_Activity.this);
//            }
//        }).start();
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.back:
//                finish();
//                break;
//            case R.id.add_Button:
//                List<CalendarSportNotice> calendarNoticelist = gson.fromJson(sportJson,new TypeToken<List<CalendarSportNotice>>(){}.getType());
//                for (CalendarSportNotice calendarSportNotice : calendarNoticelist){
//                    calendarSportNoticeList.add(calendarSportNotice);
//                }
//                adapter.setViewList(calendarSportNoticeList);
//                adapter.notifyDataSetChanged();
//                break;
//            case R.id.save_Button:
//                calendarSportNoticeList = adapter.getList();
//                String json = new Gson().toJson(calendarSportNoticeList);
//                PostSubmit(json);
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == MEASUREMENT_REPEAT){
//            int position = data.getIntExtra("position",0);
//            String name = data.getStringExtra("name");
//            String repeat = data.getStringExtra("repeat");
//            calendarSportNoticeList.get(position).getInfoAry().get(4).setrText(name);
//            calendarSportNoticeList.get(position).getInfoAry().get(4).setRepeat(repeat);
//            adapter.setViewList(calendarSportNoticeList);
//        }
//    }
//}
