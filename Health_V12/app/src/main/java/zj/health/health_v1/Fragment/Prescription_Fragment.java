package zj.health.health_v1.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import zj.health.health_v1.Activity.Take_medicine_Activity;
import zj.health.health_v1.Adapter.Prescription_DoctorFragment_Adapter;
import zj.health.health_v1.Adapter.Prescription_Fragment_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.PrescModel;
import zj.health.health_v1.Model.ReminderListModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Service.AlarmManageService;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/16.
 */

public class Prescription_Fragment extends Fragment{

    private View view = null;
    private List<PrescModel> prescModelList = new ArrayList<>();
    private RecyclerView prescription_recy;
    private Prescription_Fragment_Adapter adapter = null;
    private Handler prescHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        prescModelList = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<PrescModel>>(){}.getType());
                        if(prescModelList.size() == 0){
                            prescModelList.add(new PrescModel());
                        }
                        adapter.setPrescModelList(prescModelList);
                    }else{
                        Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
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
    private Handler PrescDetailHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        String remark = jsonObject1.optString("remark");
                        List<ReminderListModel> list = new Gson().fromJson(jsonObject1.optString("medicines"),new TypeToken<List<ReminderListModel>>(){}.getType());
                        Calendar calendar = Calendar.getInstance();
                        for (int i = 0;i<list.size();i++){
                            list.get(i).setPrescId(null);
                            list.get(i).setId(null);
                            list.get(i).setStartTime(System.currentTimeMillis()+"");
                            if(list.get(i).getTreatment().equals("1")){
                                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)+1,23,59);
                            }else if(list.get(i).getTreatment().equals("2")){
                                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)+3,23,59);
                            }else if(list.get(i).getTreatment().equals("3")){
                                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)+7,23,59);
                            }else{
                                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+2,calendar.get(Calendar.DAY_OF_MONTH),23,59);
                            }
                            list.get(i).setEndTime(calendar.getTimeInMillis()+"");
                            if(!StringUtil.isEmpty(remark)){
                                list.get(i).setRemark(remark);
                            }
                        }

                        String json = new Gson().toJson(list);
                        Commit(json);

                    }else{
                        Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
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
    private Handler CommitreminderHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        Intent it = new Intent(getActivity(), AlarmManageService.class);
                        getActivity().startService(it);
                        Toast.makeText(getActivity(), R.string.add_success, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rescription_ragment,container,false);
        initView();
        setData();
        return view;
    }
    private void initView(){
        prescription_recy = (RecyclerView)view.findViewById(R.id.prescription_recy);
    }

    private void setData(){

        adapter = new Prescription_Fragment_Adapter((BaseActivity) getActivity(),prescModelList);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                getPrescDetail(position);
            }
        });
        prescription_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        prescription_recy.setAdapter(adapter);
        getPrescList();
    }

    private void getPrescList(){
        String json = "type=0";
        new PostUtils().Get(Constant.getUserSuggestion+json,true,prescHandler,getActivity());
    }
    private void getPrescDetail(int position){
        String json = "suggestionId="+prescModelList.get(position).getId();
        new PostUtils().Get(Constant.getUserSuggestionDetail+json,true,PrescDetailHandler,getActivity());
    }
    private void Commit(String json){
        new PostUtils().JsonPost(Constant.commitRemind,json,CommitreminderHandler, (BaseActivity) getActivity());
    }
}
