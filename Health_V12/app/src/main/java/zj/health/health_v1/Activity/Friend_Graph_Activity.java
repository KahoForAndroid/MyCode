package zj.health.health_v1.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.github.mikephil.charting.data.BaseEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tencent.tls.tools.MD5;
import zj.health.health_v1.Adapter.FriendGraph_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Model.Chart;
import zj.health.health_v1.Model.FriendGraphicDayModel;
import zj.health.health_v1.Model.FriendGraphicModel;
import zj.health.health_v1.Model.NewGraphicModel;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.SetFriend_Graph_Util;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/10/25.
 */

public class Friend_Graph_Activity extends BaseActivity implements View.OnClickListener{

    private TextView title,time_title_text;
    private ImageView back;
    private Button week_button,month_button,year_button;
    private RecyclerView recyclerView;
    private int graphicType  = 1;
    private FriendGraph_Adapter adapter;
    private List<FriendGraphicModel.TypeData> list = new ArrayList<>();
    private List<FriendGraphicDayModel> DayList = new ArrayList<>();
    private FriendGraphicDayModel friendGraphicDayModel = new FriendGraphicDayModel();
    private List<List<Chart>> chartLists = new ArrayList<>();
    private Calendar startCalendar;//开始时间参数
    private Calendar endCalendar;//结束时间参数
    private Calendar calendar;//设置时间
    private DateUtil dateUtil = new DateUtil();
    private List<Chart> charts = new ArrayList<>();
    private SetFriend_Graph_Util friend_graph_util;

    private Handler graphHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                JSONObject jsonObject = null;
                try {
//                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                    jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")) {
                        charts.clear();

                        if(graphicType != 1){
                            FriendGraphicModel friendGraphicModels = new Gson().fromJson(jsonObject.optString("data"),FriendGraphicModel.class);
                            list.clear();
                            if(friendGraphicModels!=null){
                                if(friendGraphicModels.getWeight()!=null){
                                    friendGraphicModels.getWeight().setTitle(getString(R.string.weight));
                                    friendGraphicModels.getWeight().setType(2);
                                    list.add(friendGraphicModels.getWeight());
                                }
                                if(friendGraphicModels.getBlood_glucose()!=null){
                                    friendGraphicModels.getBlood_glucose().setType(5);
                                    friendGraphicModels.getBlood_glucose().setTitle(getString(R.string.blood_sugar));
                                    list.add(friendGraphicModels.getBlood_glucose());
                                }
                                if(friendGraphicModels.getBlood_pressure()!=null){
                                    friendGraphicModels.getBlood_pressure().setType(1);
                                    friendGraphicModels.getBlood_pressure().setTitle(getString(R.string.blood_pressure));
                                    friendGraphicModels.getBlood_pressure().setLable(getString(R.string.systolic)+","+
                                            getString(R.string.diastolic));
                                    list.add(friendGraphicModels.getBlood_pressure());
                                }
                                if(friendGraphicModels.getHeart_rate()!=null){
                                    friendGraphicModels.getHeart_rate().setType(4);
                                    friendGraphicModels.getHeart_rate().setTitle(getString(R.string.heart_rate));
                                    list.add(friendGraphicModels.getHeart_rate());
                                }
                                if(friendGraphicModels.getTemp()!=null){
                                    friendGraphicModels.getTemp().setType(3);
                                    friendGraphicModels.getTemp().setTitle(getString(R.string.temperature));
                                    list.add(friendGraphicModels.getTemp());
                                }
                            }
                        }else{
                            DayList.clear();
                            friendGraphicDayModel = new Gson().fromJson(jsonObject.optString("data"),
                                    FriendGraphicDayModel.class);
                            if(friendGraphicDayModel!=null){
                                if(friendGraphicDayModel.getWeight()!=null){
                                    if(friendGraphicDayModel.getWeight().size()>0){
                                        FriendGraphicDayModel model = new FriendGraphicDayModel();
                                        model.setWeight(friendGraphicDayModel.getWeight());
                                        model.getWeight().get(0).setTitle(getString(R.string.weight));
                                        model.getWeight().get(0).setType(2);
                                        model.setType(2);
                                        model.setTitle(getString(R.string.weight));
                                        DayList.add(model);
                                    }else{
                                        FriendGraphicDayModel.GraphicDay graphicDay = new FriendGraphicDayModel.GraphicDay();
                                        graphicDay.setTitle(getString(R.string.weight));
                                        graphicDay.setType(2);
                                        List<FriendGraphicDayModel.GraphicDay> friendgraphicList = new ArrayList<>();
                                        friendgraphicList.add(graphicDay);
                                        FriendGraphicDayModel friendGraphicDayModel = new FriendGraphicDayModel();
                                        friendGraphicDayModel.setWeight(friendgraphicList);
                                        friendGraphicDayModel.setType(2);
                                        friendGraphicDayModel.setTitle(getString(R.string.weight));
                                        DayList.add(friendGraphicDayModel);
                                    }
                                }
                                if(friendGraphicDayModel.getBlood_glucose()!=null){
                                    if(friendGraphicDayModel.getBlood_glucose().size()>0) {
                                        FriendGraphicDayModel model = new FriendGraphicDayModel();
                                        model.setBlood_glucose(friendGraphicDayModel.getBlood_glucose());
                                        model.getBlood_glucose().get(0).setType(5);
                                        model.getBlood_glucose().get(0).setTitle(getString(R.string.blood_sugar));
                                        model.setType(5);
                                        model.setTitle(getString(R.string.blood_sugar));
                                        DayList.add(model);
                                    }else{
                                        FriendGraphicDayModel.GraphicDay graphicDay = new FriendGraphicDayModel.GraphicDay();
                                        FriendGraphicDayModel friendGraphicDayModel = new FriendGraphicDayModel();
                                        graphicDay.setTitle(getString(R.string.blood_sugar));
                                        graphicDay.setType(5);
                                        List<FriendGraphicDayModel.GraphicDay> friendgraphicList = new ArrayList<>();
                                        friendgraphicList.add(graphicDay);
                                        friendGraphicDayModel.setBlood_glucose(friendgraphicList);
                                        friendGraphicDayModel.setType(5);
                                        friendGraphicDayModel.setTitle(getString(R.string.blood_sugar));
                                        DayList.add(friendGraphicDayModel);
                                    }
                                }
                                if(friendGraphicDayModel.getBlood_pressure()!=null ){
                                    if(friendGraphicDayModel.getBlood_pressure().size()>0) {
                                        FriendGraphicDayModel model = new FriendGraphicDayModel();
                                        model.setBlood_pressure(friendGraphicDayModel.getBlood_pressure());
                                        model.getBlood_pressure().get(0).setType(1);
                                        model.getBlood_pressure().get(0).setTitle(getString(R.string.blood_pressure));
                                        model.setType(1);
                                        model.setTitle(getString(R.string.blood_pressure));
                                        DayList.add(model);
                                    }else{
                                        FriendGraphicDayModel friendGraphicDayModel = new FriendGraphicDayModel();
                                        FriendGraphicDayModel.GraphicDay graphicDay = new FriendGraphicDayModel.GraphicDay();
                                        graphicDay.setTitle(getString(R.string.blood_pressure));
                                        graphicDay.setType(1);
                                        List<FriendGraphicDayModel.GraphicDay> friendgraphicList = new ArrayList<>();
                                        friendgraphicList.add(graphicDay);
                                        friendGraphicDayModel.setBlood_pressure(friendgraphicList);
                                        friendGraphicDayModel.setType(1);
                                        friendGraphicDayModel.setTitle(getString(R.string.blood_pressure));
                                        DayList.add(friendGraphicDayModel);
                                    }
                                }
                                if(friendGraphicDayModel.getHeart_rate()!=null){
                                    if(friendGraphicDayModel.getHeart_rate().size()>0) {
                                        FriendGraphicDayModel model = new FriendGraphicDayModel();
                                        model.setHeart_rate(friendGraphicDayModel.getHeart_rate());
                                        model.getHeart_rate().get(0).setType(4);
                                        model.getHeart_rate().get(0).setTitle(getString(R.string.heart_rate));
                                        model.setType(4);
                                        model.setTitle(getString(R.string.heart_rate));
                                        DayList.add(model);
                                    }else{
                                        FriendGraphicDayModel friendGraphicDayModel = new FriendGraphicDayModel();
                                        FriendGraphicDayModel.GraphicDay graphicDay = new FriendGraphicDayModel.GraphicDay();
                                        graphicDay.setTitle(getString(R.string.heart_rate));
                                        graphicDay.setType(4);
                                        List<FriendGraphicDayModel.GraphicDay> friendgraphicList = new ArrayList<>();
                                        friendgraphicList.add(graphicDay);
                                        friendGraphicDayModel.setHeart_rate(friendgraphicList);
                                        friendGraphicDayModel.setType(4);
                                        friendGraphicDayModel.setTitle(getString(R.string.heart_rate));
                                        DayList.add(friendGraphicDayModel);
                                    }
                                }

                                if(friendGraphicDayModel.getTemp()!=null) {
                                    if (friendGraphicDayModel.getTemp().size() > 0) {
                                        FriendGraphicDayModel model = new FriendGraphicDayModel();
                                        model.setTemp(friendGraphicDayModel.getTemp());
                                        model.getTemp().get(0).setType(3);
                                        model.getTemp().get(0).setTitle(getString(R.string.temperature));
                                        model.setType(3);
                                        model.setTitle(getString(R.string.temperature));
                                        DayList.add(model);
                                    } else {
                                        FriendGraphicDayModel friendGraphicDayModel = new FriendGraphicDayModel();
                                        FriendGraphicDayModel.GraphicDay graphicDay = new FriendGraphicDayModel.GraphicDay();
                                        graphicDay.setTitle(getString(R.string.temperature));
                                        graphicDay.setType(3);
                                        List<FriendGraphicDayModel.GraphicDay> friendgraphicList = new ArrayList<>();
                                        friendgraphicList.add(graphicDay);
                                        friendGraphicDayModel.setTemp(friendgraphicList);
                                        friendGraphicDayModel.setType(3);
                                        friendGraphicDayModel.setTitle(getString(R.string.temperature));
                                        DayList.add(friendGraphicDayModel);
                                    }
                                }
                            }
                        }


                          if(list.size()>0){
                              setHandlerData(list.get(0));
                          }else{
                              setHandlerData(new FriendGraphicModel.TypeData());
                          }

                          if(graphicType == 1){
                              for (int i = 0;i<DayList.size();i++){
                                  if(DayList.get(i).getType()== 1){
                                      charts.add(friend_graph_util.setBloodChatData(graphicType,null,DayList.get(i),startCalendar,19));
                                  }else if(DayList.get(i).getType()== 2){
                                      charts.add(friend_graph_util.setWeightChatData(graphicType,null,DayList.get(i),startCalendar,9));
                                  }else if(DayList.get(i).getType()== 3){
                                      charts.add(friend_graph_util.setTempChatData(graphicType,null,DayList.get(i),startCalendar,3.9f));
                                  }else if(DayList.get(i).getType()== 4){
                                      charts.add(friend_graph_util.setHeart_RateChatData(graphicType,null,DayList.get(i),startCalendar,19));
                                  }else if(DayList.get(i).getType()== 5){
                                      charts.add(friend_graph_util.setSugarChatData(graphicType,null,DayList.get(i),startCalendar,0.9f));
                                  }
                              }
                              adapter.setDataSize(null,charts,graphicType,DayList.size(),DayList);
                          }else{
                              for (int i = 0;i<list.size();i++){
                                  if(list.get(i).getType() == 1){
                                      charts.add(friend_graph_util.setBloodChatData(graphicType,list.get(i),null,startCalendar,19));
                                  }else if(list.get(i).getType()== 2 ){
                                      charts.add(friend_graph_util.setWeightChatData(graphicType,list.get(i),null,startCalendar,9));
                                  }else if(list.get(i).getType()== 3 ){
                                      charts.add(friend_graph_util.setTempChatData(graphicType,list.get(i),null,startCalendar,3.9f));
                                  }else if(list.get(i).getType()== 4 ){
                                      charts.add(friend_graph_util.setHeart_RateChatData(graphicType,list.get(i),null,startCalendar,19));
                                  }else if(list.get(i).getType()== 5 ){
                                      charts.add(friend_graph_util.setSugarChatData(graphicType,list.get(i),null,startCalendar,0.9f));
                                  }
                              }
                              adapter.setDataSize(list,charts,graphicType,list.size(),null);
                          }


                    }
                } catch (JSONException e) {
//                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
//                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(Friend_Graph_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else {
//                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(Friend_Graph_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_graph_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        title = (TextView)findViewById(R.id.title);
        time_title_text = (TextView)findViewById(R.id.time_title_text);
        back = (ImageView)findViewById(R.id.back);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        week_button = (Button)findViewById(R.id.week_button);
        month_button = (Button)findViewById(R.id.month_button);
        year_button = (Button)findViewById(R.id.year_button);
    }
    private void BindListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        week_button.setOnClickListener(this);
        month_button.setOnClickListener(this);
        year_button.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.friend_data));
        friend_graph_util = new SetFriend_Graph_Util();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendGraph_Adapter(this,list,charts);
        recyclerView.setAdapter(adapter);

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        calendar = Calendar.getInstance();;
        calendar.set(startCalendar.get(Calendar.YEAR),0,1);

        Calendar calendar_list_start = Calendar.getInstance();
        calendar_list_start.set(startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH),0,0,1);
        Calendar calendar_list_end = Calendar.getInstance();
        calendar_list_end.set(endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH),23,59,0);
//        getFriendGraph(calendar_list_start.getTimeInMillis(),calendar_list_end.getTimeInMillis());
        getGraph_for_day(startCalendar.getTimeInMillis());

    }

    private void setHandlerData(FriendGraphicModel.TypeData typeData){
        if(graphicType == 1){
            time_title_text.setText(startCalendar.get(Calendar.DAY_OF_MONTH)+"号");

        }else if(graphicType == 2){
            if(typeData.getMonthly()!=null && typeData.getMonthly().size()>0){
                time_title_text.setText(typeData.getMonthly().get(0).getMonth()+"月");
            }else{
                time_title_text.setText((startCalendar.get(Calendar.MONTH)+1)+"月");
            }

        }else if(graphicType == 3){
            if(typeData.getYear()!=null && typeData.getYear().size()>0){
                time_title_text.setText(typeData.getYear().get(0).getYear()+"年");
            }else{
                time_title_text.setText(startCalendar.get(Calendar.YEAR)+"年");
            }

        }else{
            time_title_text.setText("");
        }
    }

    /**
     * 查询某天的所有图表数据(折线数据)
     * @param Today
     */
    private void getGraph_for_day(long Today){
        String data = "st"+"="+Today+"&friendId="+getIntent().getStringExtra("friendId");
        new PostUtils().Get(Constant.check_friend_graph_Day+data,true,graphHandler,this);
    }


    private void getFriendGraph(long StartTime,long endTime){
        String json = "st="+StartTime+"&et="+endTime+"&friendId="+getIntent().getStringExtra("friendId");
        new PostUtils().Get(Constant.check_friend_graph_list+json,true,graphHandler,this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.week_button:
                graphicType = 1;
                dateUtil.ShowTimePickerView(this,onTimeSelectListener,calendar,
                        true,true,true,false,false,false).show();

                break;
            case R.id.month_button:
                graphicType = 2;
                dateUtil.ShowTimePickerView(this,onTimeSelectListener,calendar,
                        true,true,false,false,false,false).show();
                break;
            case R.id.year_button:
                graphicType = 3;
                dateUtil.ShowTimePickerView(this,onTimeSelectListener,calendar,
                        true,false,false,false,false,false).show();
                break;
                default:
                    break;
        }
    }

    private TimePickerView.OnTimeSelectListener onTimeSelectListener = new TimePickerView.OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, View v) {
            switch (graphicType){
                case 1:
                    graphicType = 1;
                    week_button.setBackgroundResource(R.drawable.radius_blue2);
                    month_button.setBackgroundResource(R.drawable.radius_gary);
                    year_button.setBackgroundResource(R.drawable.radius_gary);
//                    LoadingDialog.getLoadingDialog().StartLoadingDialog(Friend_Graph_Activity.this);
                    startCalendar.setTime(date);
                    endCalendar.setTime(date);


                    Calendar calendar_list_start = Calendar.getInstance();
                    calendar_list_start.setTime(date);
                    calendar_list_start.set(calendar_list_start.get(Calendar.YEAR),calendar_list_start.get(Calendar.MONTH),
                            calendar_list_start.get(Calendar.DAY_OF_MONTH),0,0,1);
                    Calendar calendar_list_end = Calendar.getInstance();
                    calendar_list_end.setTime(date);
                    calendar_list_end.set(calendar_list_end.get(Calendar.YEAR),calendar_list_end.get(Calendar.MONTH),
                            calendar_list_end.get(Calendar.DAY_OF_MONTH),23,59,0);
//                    getFriendGraph(calendar_list_start.getTimeInMillis(),calendar_list_end.getTimeInMillis());
                    getGraph_for_day(startCalendar.getTimeInMillis());

                    break;
                case 2:
                    graphicType = 2;
                    week_button.setBackgroundResource(R.drawable.radius_gary);
                    month_button.setBackgroundResource(R.drawable.radius_blue2);
                    year_button.setBackgroundResource(R.drawable.radius_gary);
                    startCalendar.setTime(date);
                    endCalendar.setTime(date);
                    startCalendar.set(startCalendar.get(Calendar.YEAR),startCalendar.
                            get(Calendar.MONTH),startCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                    endCalendar.set(endCalendar.get(Calendar.YEAR),endCalendar.
                            get(Calendar.MONTH),startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    getFriendGraph(startCalendar.getTimeInMillis(),endCalendar.getTimeInMillis());
                    break;
                case 3:
                    graphicType = 3;
                    week_button.setBackgroundResource(R.drawable.radius_gary);
                    month_button.setBackgroundResource(R.drawable.radius_gary);
                    year_button.setBackgroundResource(R.drawable.radius_blue2);
                    startCalendar.setTime(date);
                    endCalendar.setTime(date);
                    startCalendar.set(startCalendar.get(Calendar.YEAR),0,1);
                    endCalendar.set(endCalendar.get(Calendar.YEAR),11,31);
                    getFriendGraph(startCalendar.getTimeInMillis(),endCalendar.getTimeInMillis());
                    break;
                default:
                    break;
            }
        }
    };
}
