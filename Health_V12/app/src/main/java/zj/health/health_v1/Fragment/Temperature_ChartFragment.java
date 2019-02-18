package zj.health.health_v1.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BaseEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Activity.Commit_DoctorData_Activity;
import zj.health.health_v1.Adapter.Body_ChartList_Adapter;
import zj.health.health_v1.Adapter.ChartFragment_Adapter;
import zj.health.health_v1.Adapter.Heart_Rate_ChartFragmrnt_Adapter;
import zj.health.health_v1.Adapter.Temperature_ChartFragment_Adapter;
import zj.health.health_v1.Adapter.Weight_ChartFragment_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.BodyChartListModel;
import zj.health.health_v1.Model.Chart;
import zj.health.health_v1.Model.GraphicModel;
import zj.health.health_v1.Model.NewGraphicModel;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.ChartUtil;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/6/27.
 */

public class Temperature_ChartFragment extends Fragment implements View.OnClickListener{


    private View view = null;
    private RecyclerView recyclerView;
    private Temperature_ChartFragment_Adapter adapter;
    private List<Chart> charts;
    private ImageView previous_image,next_image;
    private Button week_button,month_button,year_button;
    private TextView time_title_text;
    private Gson gson = new Gson();
    private int graphicType  = 1;
    private NewGraphicModel newGraphicModel = new NewGraphicModel();
    private Calendar startCalendar;//开始时间参数
    private Calendar endCalendar;//结束时间参数
    private Calendar calendar;//设置时间
    private DateUtil dateUtil = new DateUtil();
    private PopupWindow popupWindow;
    private List<BodyChartListModel> bodyChartListModelList = new ArrayList<>();
    private Body_ChartList_Adapter list_adapter;
    private RecyclerView recyclerView_list;

    private Handler TempHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")) {
                        if(graphicType == 1){
                            List<NewGraphicModel.GraphicDay> list = gson.fromJson(jsonObject.optString("data"),
                                    new TypeToken<List<NewGraphicModel.GraphicDay>>(){}.getType());
                            NewGraphicModel.GraphicDay graphicDay = new NewGraphicModel.GraphicDay();

                            newGraphicModel.setGraphicDayList(list);
                        }else{
                            newGraphicModel = gson.fromJson(jsonObject.optString("data"),
                                    NewGraphicModel.class);
                        }
                        if(newGraphicModel!=null){
                            newGraphicModel.setLable(getString(R.string.Degrees));
                            newGraphicModel.setTitle(getString(R.string.temperature));
                            setHandlerData(newGraphicModel);
                        }
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                }
            }else if(msg.what == Constant.UserErrorCode){
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else {
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };


    private Handler TempList_pressureHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")) {
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        bodyChartListModelList = gson.fromJson(jsonObject1.optString("list"),new TypeToken<List<BodyChartListModel>>(){}.getType());
                        list_adapter.setBodyChartListModelList(bodyChartListModelList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler Update_TempList_pressureHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                JSONObject jsonObject = null;
                try {
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                    jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")) {

                        getTemp_for_day(startCalendar.getTimeInMillis());

                        Calendar calendar_list_start = Calendar.getInstance();
                        calendar_list_start.set(startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),
                                startCalendar.get(Calendar.DAY_OF_MONTH),0,0,1);
                        Calendar calendar_list_end = Calendar.getInstance();
                        calendar_list_end.set(endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),
                                endCalendar.get(Calendar.DAY_OF_MONTH),23,59,0);
                        getTemp_for_List(calendar_list_start.getTimeInMillis(),calendar_list_end.getTimeInMillis());
                    }
                } catch (JSONException e) {
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else {
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chart_fragment,container,false);
        initView();
        BindListener();
        setData();
        return view;
    }

    private void initView(){

//        lineChart = (LineChart)view.findViewById(R.id.lineChart);
//        lineChart2 = (LineChart)view.findViewById(R.id.lineChart2);
//        BarChart = (com.github.mikephil.charting.charts.BarChart)view.findViewById(R.id.barChart);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        previous_image = (ImageView)view.findViewById(R.id.previous_image);
        next_image = (ImageView)view.findViewById(R.id.next_image);
        week_button = (Button)view.findViewById(R.id.week_button);
        month_button = (Button)view.findViewById(R.id.month_button);
        year_button = (Button)view.findViewById(R.id.year_button);
        time_title_text = (TextView)view.findViewById(R.id.time_title_text);
        recyclerView_list = (RecyclerView)view.findViewById(R.id.recyclerView_list);
    }

    private void BindListener(){
        previous_image.setOnClickListener(this);
        next_image.setOnClickListener(this);
        week_button.setOnClickListener(this);
        month_button.setOnClickListener(this);
        year_button.setOnClickListener(this);
    }

    private void setData(){
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView_list.setNestedScrollingEnabled(false);
        charts = new ArrayList<>();
        adapter = new Temperature_ChartFragment_Adapter(getActivity(),charts,newGraphicModel);
        list_adapter = new Body_ChartList_Adapter(getActivity(),bodyChartListModelList,3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_list.setAdapter(list_adapter);
        list_adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                Edit_chart_Dailog(getActivity(),View.inflate(getActivity(),R.layout.chart_list_edit_dialog3,null),position);
            }
        });


        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        calendar = Calendar.getInstance();;
        calendar.set(2018,0,1);
        getTemp_for_day(startCalendar.getTimeInMillis());


        Calendar calendar_list_start = Calendar.getInstance();
        calendar_list_start.set(startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH),0,0,1);
        Calendar calendar_list_end = Calendar.getInstance();
        calendar_list_end.set(endCalendar.get(Calendar.YEAR),endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH),23,59,0);
        getTemp_for_List(calendar_list_start.getTimeInMillis(),calendar_list_end.getTimeInMillis());

    }

    private void setHandlerData(NewGraphicModel newGraphicModel){
        if(graphicType == 1){
            time_title_text.setText(startCalendar.get(Calendar.DAY_OF_MONTH)+"号");

        }else if(graphicType == 2){
            if(newGraphicModel.getMonthly().size()>0){
                time_title_text.setText(newGraphicModel.getMonthly().get(0).getMonth()+"月");
            }else{
                time_title_text.setText((startCalendar.get(Calendar.MONTH)+1)+"月");
            }

        }else if(graphicType == 3){
            if(newGraphicModel.getYear().size()>0){
                time_title_text.setText(newGraphicModel.getYear().get(0).getYear()+"年");
            }else{
                time_title_text.setText(startCalendar.get(Calendar.YEAR)+"年");
            }

        }else{
            time_title_text.setText("");
        }
        setChatData(graphicType);
    }


    private void setChatData(int graphicType){
        charts.clear();
        Chart chart = new Chart();
        List<String> xstringList = new ArrayList<>();
        List<List<? extends BaseEntry>> entriesList = new ArrayList<>();
        List<Entry> entriesys_Average = new ArrayList<>();


        switch(graphicType){
            case 1:
                for (int day = 0;day<25;day++){
                    xstringList.add(day+"");
                }
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                if(newGraphicModel.getGraphicDayList().size()>0) {

                    float Total = 0;
                    for (int i = 0;i<newGraphicModel.getGraphicDayList().size();i++){
                        Total += Float.parseFloat(newGraphicModel.getGraphicDayList().get(i).getVal());
                    }
                    float average1 = Total/newGraphicModel.getGraphicDayList().size();
                    newGraphicModel.setAverage(average1+"");

                    date.setTime(Long.parseLong(newGraphicModel.getGraphicDayList().get(0).getTimestamp()));
                    calendar.setTime(date);
                    int Firstx = calendar.get(Calendar.HOUR_OF_DAY);
                    if(Firstx!=1){
                        entriesys_Average.add(new Entry(1, average1));
                    }

                    for (int i = 0; i < newGraphicModel.getGraphicDayList().size(); i++) {
                        date.setTime(Long.parseLong(newGraphicModel.getGraphicDayList().get(i).getTimestamp()));
                        calendar.setTime(date);
                        int x = calendar.get(Calendar.HOUR_OF_DAY);
                        entriesys_Average.add(new Entry(Float.parseFloat(x+"."+calendar.get(Calendar.MINUTE)), Float.valueOf(newGraphicModel.getGraphicDayList().get(i).getVal())));
                    }

                    date.setTime(Long.parseLong(newGraphicModel.getGraphicDayList().
                            get(newGraphicModel.getGraphicDayList().size()-1).getTimestamp()));
                    calendar.setTime(date);
                    if(calendar.get(Calendar.HOUR)<24){
                        entriesys_Average.add(new Entry(24, average1));

                    }

                }else {
                    for (int i = 1;i<25;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(3.9f)));
                    }

                }

                entriesList.add(entriesys_Average);
                break;
            case 2:
                int DayForMonth = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int day = 0;day<DayForMonth+1;day++){
                    xstringList.add(day+"");
                }
                if(newGraphicModel.getDaily().size()>0) {

                    if(Integer.parseInt(newGraphicModel.getDaily().get(0).getDay())!=1){
                        entriesys_Average.add(new Entry(1, Float.parseFloat(newGraphicModel.getMonthly().get(0).getAverage())));
                    }

                    for (int i = 0; i < newGraphicModel.getDaily().size(); i++) {

                        entriesys_Average.add(new Entry(Float.parseFloat(newGraphicModel.getDaily().get(i).getDay()),
                                Float.valueOf(newGraphicModel.getDaily().get(i).getAverage())));


                    }

                    if(Integer.parseInt(newGraphicModel.getDaily().get(newGraphicModel.getDaily().size()-1).getDay())<DayForMonth){
                        entriesys_Average.add(new Entry(DayForMonth+1, Float.parseFloat(newGraphicModel.getMonthly().get(0).getAverage())));
                    }

                }else {
                    for (int i = 1;i<DayForMonth+1;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(3.9f)));
                    }
                }
                entriesList.add(entriesys_Average);
                break;
            case 3:

                for (int i = 0;i<13;i++){
                    xstringList.add(i+"");
                }
                if(newGraphicModel.getMonthly().size()>0) {

                    if(Integer.parseInt(newGraphicModel.getMonthly().get(0).getMonth())!=1){
                        entriesys_Average.add(new Entry(1, Float.parseFloat(newGraphicModel.getYear().get(0).getAverage())));
                    }

                    for (int i = 0; i < newGraphicModel.getMonthly().size(); i++) {

                        entriesys_Average.add(new Entry(Float.parseFloat(newGraphicModel.getMonthly().get(i).getMonth()),
                                Float.valueOf(newGraphicModel.getMonthly().get(i).getAverage())));


                    }

                    if(Integer.parseInt(newGraphicModel.getMonthly().
                            get(newGraphicModel.getMonthly().size()-1).getMonth())< 12){
                        entriesys_Average.add(new Entry(12, Float.parseFloat(newGraphicModel.getYear().get(0).getAverage())));
                    }

                }else {
                    for (int i = 1;i<13;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(3.9f)));
                    }
                }
                entriesList.add(entriesys_Average);

                break;
            default:
                break;
        }
        chart.setEntriesList(entriesList);
        chart.setxStringType(xstringList);
        chart.setType(0);
        charts.add(chart);
        adapter.setData(charts,newGraphicModel,graphicType);

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
                    LoadingDialog.getLoadingDialog().StartLoadingDialog(getActivity());
                    startCalendar.setTime(date);
                    endCalendar.setTime(date);
                    getTemp_for_day(startCalendar.getTimeInMillis());


                    Calendar calendar_list_start = Calendar.getInstance();
                    calendar_list_start.setTime(date);
                    calendar_list_start.set(calendar_list_start.get(Calendar.YEAR),calendar_list_start.get(Calendar.MONTH),
                            calendar_list_start.get(Calendar.DAY_OF_MONTH),0,0,1);
                    Calendar calendar_list_end = Calendar.getInstance();
                    calendar_list_end.setTime(date);
                    calendar_list_end.set(calendar_list_end.get(Calendar.YEAR),calendar_list_end.get(Calendar.MONTH),
                            calendar_list_end.get(Calendar.DAY_OF_MONTH),23,59,0);
                    getTemp_for_List(calendar_list_start.getTimeInMillis(),calendar_list_end.getTimeInMillis());

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
                    getTemp(startCalendar.getTimeInMillis(),endCalendar.getTimeInMillis());
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
                    getTemp(startCalendar.getTimeInMillis(),endCalendar.getTimeInMillis());
                    break;
                default:
                    break;
            }
        }
    };


    private void getTemp(long startDate,long endDate){
        String data = "st"+"="+startDate+"&"+"et"+"="+endDate;
        new PostUtils().Get(Constant.temp+data,true,TempHandler,getActivity());
    }

    private void getTemp_for_day(long Today){
        String data = "st"+"="+Today;
        new PostUtils().Get(Constant.temp_day+data,true,TempHandler,getActivity());
    }


    /**
     * 获取指定日期的图表数据(列表数据)
     * 下面参数指的是时间区间 搜索指定时间范围内的数据
     * @param StartTime 开始时间
     * @param endTime 结束时间
     */
    private void getTemp_for_List(long StartTime,long endTime){
        String data = "st"+"="+StartTime+"&et="+endTime+"&page=0&perPage=20";
        new PostUtils().Get(Constant.temp_list+data,true,TempList_pressureHandler,getActivity());
    }

    /**
     * 更新图表录入模板数据
     * @param json 以json格式上传
     */
    private void Update_Temp_pressure_list(String json){
        LoadingDialog.getLoadingDialog().StartLoadingDialog(getActivity());
        new PostUtils().JsonPost(Constant.update_or_delete_temp_list,json,Update_TempList_pressureHandler, (BaseActivity) getActivity());
    }

    /**
     * 删除某一块录入数据
     * @param id 数据ID
     */
    private void Delete_Temp_list(String id){
        LoadingDialog.getLoadingDialog().StartLoadingDialog(getActivity());
        String json = "id="+id;
        new PostUtils().Delete(Constant.update_or_delete_temp_list+json,true,Update_TempList_pressureHandler, (BaseActivity) getActivity());
    }


    /**
     * 修改数据弹出框
     * @param context 上下文
     * @param view view
     */
    public void Edit_chart_Dailog(Context context, final View view, final int position){

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popwindow_bg));
        popupWindow.setOutsideTouchable(true);

        final TextView body_type = (TextView)view.findViewById(R.id.body_type);
        final EditText type_text = (EditText)view.findViewById(R.id.type_text);
        Button saveButton = (Button)view.findViewById(R.id.saveButton);
        Button deleteButton = (Button)view.findViewById(R.id.deleteButton);
        Button cancelButton = (Button)view.findViewById(R.id.cancelButton);

        body_type.setText(getString(R.string.temperature));
        type_text.setText(bodyChartListModelList.get(position).getTemp());
        type_text.setSelection(type_text.getText().length());


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type_text.getText().toString().length()>0){
                    if(popupWindow!=null){
                        popupWindow.dismiss();
                    }
                    BodyChartListModel bodyChartListModel = bodyChartListModelList.get(position);
                    bodyChartListModel.setTemp(type_text.getText().toString());
                    Update_Temp_pressure_list(gson.toJson(bodyChartListModel));
                }else{
                    Toast.makeText(getActivity(), R.string.check_Omission, Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow!=null){
                    popupWindow.dismiss();
                }
                Delete_Temp_list(bodyChartListModelList.get(position).getId());
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.dismiss();

                }
            }
        });

        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.week_button:
                graphicType = 1;
                dateUtil.ShowTimePickerView(getActivity(),onTimeSelectListener,calendar,
                        true,true,true,false,false,false).show();

                break;
            case R.id.month_button:
                graphicType = 2;
                dateUtil.ShowTimePickerView(getActivity(),onTimeSelectListener,calendar,
                        true,true,false,false,false,false).show();
                break;
            case R.id.year_button:
                graphicType = 3;
                dateUtil.ShowTimePickerView(getActivity(),onTimeSelectListener,calendar,
                        true,false,false,false,false,false).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 3){
//            getTemp_for_day(startCalendar.getTimeInMillis());  switch (graphicType){
            switch (graphicType){
                case 1:
                    getTemp_for_day(startCalendar.getTimeInMillis());

                    Calendar calendar_list_start = Calendar.getInstance();
                    calendar_list_start.set(startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),
                            startCalendar.get(Calendar.DAY_OF_MONTH),0,0,1);
                    Calendar calendar_list_end = Calendar.getInstance();
                    calendar_list_end.set(startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),
                            startCalendar.get(Calendar.DAY_OF_MONTH),23,59,0);
                    getTemp_for_List(calendar_list_start.getTimeInMillis(),calendar_list_end.getTimeInMillis());


                    break;
                case 2:
                    getTemp(startCalendar.getTimeInMillis(),endCalendar.getTimeInMillis());
                    break;
                case 3:
                    getTemp(startCalendar.getTimeInMillis(),endCalendar.getTimeInMillis());
                    break;
                default:
                    break;
            }
        }
    }
}

