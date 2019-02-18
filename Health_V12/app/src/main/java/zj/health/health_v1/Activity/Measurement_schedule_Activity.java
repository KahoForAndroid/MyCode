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
//import zj.health.health_v1.Adapter.Take_medicine_Adapter;
//import zj.health.health_v1.Base.BaseActivity;
//import zj.health.health_v1.Base.Health_AppLocation;
//import zj.health.health_v1.Model.CalendarMeasureNotice;
//import zj.health.health_v1.Model.CalendarNoticeModel;
//import zj.health.health_v1.R;
//import zj.health.health_v1.Utils.HttpUtil;
//
///**
// * 测量日程页面
// * Created by Administrator on 2018/6/13.
// */
//
//public class Measurement_schedule_Activity extends BaseActivity implements View.OnClickListener{
//
//    private RecyclerView layout_recy;
//    private Take_Measurement_Adapter adapter;
//    private ImageView back;
//    private TextView title;
//    private Button add_Button,save_Button;
//    private String postName = "notification/measureSubmit?";
//    private String postDelete = "notification/notificationInfoDisable?";
//    private List<CalendarMeasureNotice> calendarMeasureNoticeList = new ArrayList<>();
//    private Bundle bundle = new Bundle();
//    private final int MEASUREMENT_REPEAT = 0x113;
//    private int deleteSize = 0;
//    private Gson gson = new Gson();
//    private String selectDay = null;
//
//    //由于数据要与小程序关联 在新建模板时 根据这个json数据进行数据封装
//    private String measurementJson = " [\n" +
//            "        {\n" +
//            "            \"notificationId\": 0,\n" +
//            "            \"id\": 0,\n" +
//            "            \"type\": 4,\n" +
//            "            \"isShowBtn\": \"true\",\n" +
//            "            \"infoAry\": [\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"测量项目\",\n" +
//            "                    \"rText\": \"血糖\",\n" +
//            "                    \"itemAry\": [\n" +
//            "                        \"血糖\",\n" +
//            "                        \"血压\",\n" +
//            "                        \"心率\",\n" +
//            "                        \"体重\"\n" +
//            "                    ],\n" +
//            "                    \"editType\": \"sheet\"\n" +
//            "                },\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"备注\",\n" +
//            "                    \"rText\": \"\",\n" +
//            "                    \"editType\": \"input\"\n" +
//            "                },\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"测量次数\",\n" +
//            "                    \"rText\": \"一日1次\",\n" +
//            "                    \"itemAry\": [\n" +
//            "                        \"一日1次\",\n" +
//            "                        \"一日2次\",\n" +
//            "                        \"一日3次\"\n" +
//            "                    ],\n" +
//            "                    \"editType\": \"sheet\"\n" +
//            "                },\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"测量时间1\",\n" +
//            "                    \"rText\": \"12:00\",\n" +
//            "                    \"editType\": \"picker-time\",\n" +
//            "                    \"isShow\": true\n" +
//            "                },\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"测量时间2\",\n" +
//            "                    \"rText\": \"18:00\",\n" +
//            "                    \"editType\": \"picker-time\",\n" +
//            "                    \"isShow\": false\n" +
//            "                },\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"测量时间3\",\n" +
//            "                    \"rText\": \"20:00\",\n" +
//            "                    \"editType\": \"picker-time\",\n" +
//            "                    \"isShow\": false\n" +
//            "                },\n" +
//            "                {\n" +
//            "                    \"lTitle\": \"日程安排\",\n" +
//            "                    \"rText\": \"星期六、日\",\n" +
//            "                    \"repeat\": \"0000011\",\n" +
//            "                    \"editType\": \"button\",\n" +
//            "                    \"url\": \"../user-defined/week?title=体检日程\"\n" +
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
//                        if(adapter.getDeleteId().size() == 0){
//                            Toast.makeText(Measurement_schedule_Activity.this,getString(R.string.save_succ), Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(Measurement_schedule_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(Measurement_schedule_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                }
//            }else{
//                Toast.makeText(Measurement_schedule_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(Measurement_schedule_Activity.this,getString(R.string.save_succ), Toast.LENGTH_SHORT).show();
//                        setResult(0x111);
//                        finish();
//                    }else{
//                        Toast.makeText(Measurement_schedule_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(Measurement_schedule_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
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
//        title.setText(getString(R.string.Physical_examination_schedule));
//        if(getIntent().getBundleExtra("bundle")!=null){
//            selectDay = getIntent().getStringExtra("date");
//            bundle = getIntent().getBundleExtra("bundle");
//            calendarMeasureNoticeList = (List<CalendarMeasureNotice>) bundle.getSerializable("list");
//        }else{
//            selectDay = getIntent().getStringExtra("date");
//            calendarMeasureNoticeList = gson.fromJson(measurementJson,new TypeToken<List<CalendarMeasureNotice>>(){}.getType());
//        }
//
//        adapter = new Take_Measurement_Adapter(this,calendarMeasureNoticeList,selectDay);
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
//                new HttpUtil().Post("http://health.zhzjzdh.com/"+postName,map,submitHandler,Measurement_schedule_Activity.this);
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
//                new HttpUtil().Post("http://health.zhzjzdh.com/"+postDelete,map,DeleteHandler,Measurement_schedule_Activity.this);
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
//                List<CalendarMeasureNotice> calendarNoticelist = gson.fromJson(measurementJson,new TypeToken<List<CalendarMeasureNotice>>(){}.getType());
//                for (CalendarMeasureNotice calendarNoticeModel1 : calendarNoticelist){
//                    calendarMeasureNoticeList.add(calendarNoticeModel1);
//                }
//                adapter.setViewList(calendarMeasureNoticeList);
//                adapter.notifyDataSetChanged();
//                break;
//            case R.id.save_Button:
//                calendarMeasureNoticeList = adapter.getList();
//                String json = new Gson().toJson(calendarMeasureNoticeList);
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
//            calendarMeasureNoticeList.get(position).getInfoAry().get(6).setrText(name);
//            calendarMeasureNoticeList.get(position).getInfoAry().get(6).setRepeat(repeat);
//            adapter.setViewList(calendarMeasureNoticeList);
//        }
//    }
//}