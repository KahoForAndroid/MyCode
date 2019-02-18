package zj.health.health_v1.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.sns.TIMAddFriendRequest;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendStatus;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.Counseling_List_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.ui.ChatActivity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.ConsultListModel;
import zj.health.health_v1.Model.ConsultModel;
import zj.health.health_v1.Model.CounselingListModel;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GreenDaoManager;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/8/24.
 */

public class Counselling_list_Activity extends BaseActivity{

    private ImageView back;
    private TextView title;
    private RecyclerView recy_list;
    private Counseling_List_Adapter adapter;
    private List<CounselingListModel> counselingListModelList = new ArrayList<>();
    private CounselingListModel counselingListModel;
    private Intent it = null;
    private String consultId;
    private String FaceUrl= null;
    private int ChatCount = 0;
    private String titleStr = null;
    public static Counselling_list_Activity activity;
    private float cost;
    private PushBroad pushBroad;
    private SwipeRefreshLayout swipe_view;
    private Handler Counseling_list_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                     if(jsonObject.optString("msg").equals("success")){
                        counselingListModelList = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<CounselingListModel>>(){}.getType());
                        if(counselingListModelList!=null && counselingListModelList.size()>0){
                            adapter.setCounselingListModelList(counselingListModelList);
                        }else{
                            CreateDialog dialog = new CreateDialog();
                            dialog.MessageDailog2(Counselling_list_Activity.this,
                                    View.inflate(Counselling_list_Activity.this,R.layout.main_popwindow2,null),
                                    getString(R.string.wait_to_doctor_Invitation));
                        }

                         if(swipe_view.isRefreshing()){
                             swipe_view.setRefreshing(false);
                         }

                    }else{
                        Toast.makeText(Counselling_list_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                         if(swipe_view.isRefreshing()){
                             swipe_view.setRefreshing(false);
                         }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(swipe_view.isRefreshing()){
                        swipe_view.setRefreshing(false);
                    }
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Counselling_list_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
                if(swipe_view.isRefreshing()){
                    swipe_view.setRefreshing(false);
                }
            }else{
                Toast.makeText(Counselling_list_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                if(swipe_view.isRefreshing()){
                    swipe_view.setRefreshing(false);
                }
            }

        }
    };


    private Handler hissory_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        List<ConsultModel> consultModelList = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<ConsultModel>>(){}.getType());

                        if(consultModelList!=null && consultModelList.size()>0){
                            for (int i = 0;i<consultModelList.size();i++){
                                if(consultModelList.get(i).getId().equals(consultId)) {
                                    cost = consultModelList.get(i).getCost();
                                    if (Health_AppLocation.instance.users.getDoctorStatus().equals("2")) {
                                        getConsultList();
                                    } else {
                                        Intent it = new Intent(Counselling_list_Activity.this, PayActivity.class);
                                        it.putExtra("consultId", consultId);
                                        it.putExtra("doctorId", counselingListModel.getId());
                                        it.putExtra("cost", cost);
                                        startActivity(it);
                                        break;
                                    }
                                }
                            }
                        }else{
                            Toast.makeText(Counselling_list_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        Toast.makeText(Counselling_list_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Counselling_list_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Counselling_list_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };


    private Handler consult_list_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("code").equals("4")){
                        Intent it = new Intent(Counselling_list_Activity.this,PayActivity.class);
                        it.putExtra("consultId",consultId);
                        it.putExtra("doctorId",counselingListModel.getId());
                        it.putExtra("FaceUrl",FaceUrl);
                        it.putExtra("titleStr",titleStr);
                        it.putExtra("cost",cost);
                        startActivity(it);
                        return;
                    }
                    if(jsonObject.optString("msg").equals("success")){
                        List<ConsultListModel> consultListModelList = new Gson().
                                fromJson(jsonObject.optString("data"),new TypeToken<List<ConsultListModel>>(){}.getType());

                        if(consultListModelList!=null && consultListModelList.size()>0){
                            boolean equals = false;
                            for (int i = 0;i<consultListModelList.size();i++){
                                if(consultListModelList.get(i).getUserId().equals(counselingListModel.getId()) && consultListModelList.get(i).getStatus().equals("4")){
                                    equals = true;
                                    Toast.makeText(Counselling_list_Activity.this, R.string.consult_to_users_error, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if(!equals){
                                Intent it = new Intent(Counselling_list_Activity.this,PayActivity.class);
                                it.putExtra("consultId",consultId);
                                it.putExtra("doctorId",counselingListModel.getId());
                                it.putExtra("FaceUrl",FaceUrl);
                                it.putExtra("titleStr",titleStr);
                                it.putExtra("cost",cost);
                                startActivity(it);
                            }
                        }else{
                            Intent it = new Intent(Counselling_list_Activity.this,PayActivity.class);
                            it.putExtra("consultId",consultId);
                            it.putExtra("doctorId",counselingListModel.getId());
                            it.putExtra("FaceUrl",FaceUrl);
                            it.putExtra("titleStr",titleStr);
                            it.putExtra("cost",cost);
                            startActivity(it);
                        }

                    }else{
                        Toast.makeText(Counselling_list_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }else if(msg.what == Constant.UserErrorCode){

                Toast.makeText(Counselling_list_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{

                Toast.makeText(Counselling_list_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };


    private Handler consult_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());

                    if(jsonObject.optString("msg").equals("success")){
                        ConsultModel consultModel = new Gson().
                                fromJson(jsonObject.optString("data"),ConsultModel.class);

                        if(consultModel!=null){
                            cost = consultModel.getCost();
                            if(Health_AppLocation.instance.users.getDoctorStatus().equals("2")){
                                getConsultList();
                            }else{
                                Intent it = new Intent(Counselling_list_Activity.this,PayActivity.class);
                                it.putExtra("consultId",consultId);
                                it.putExtra("doctorId",counselingListModel.getId());
                                it.putExtra("FaceUrl",FaceUrl);
                                it.putExtra("titleStr",titleStr);
                                it.putExtra("cost",cost);
                                startActivity(it);
                            }
                        }else{
                            Toast.makeText(Counselling_list_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(Counselling_list_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }else if(msg.what == Constant.UserErrorCode){

                Toast.makeText(Counselling_list_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{

                Toast.makeText(Counselling_list_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ounselling_list_activity);
        activity = this;
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        recy_list = (RecyclerView)findViewById(R.id.recy_list);
        swipe_view = (SwipeRefreshLayout)findViewById(R.id.swipe_view);
    }
    private void BindListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipe_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList(consultId);
            }
        });
    }
    private void setData(){
        title.setText(getString(R.string.counseling_list));
        recy_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Counseling_List_Adapter(this,counselingListModelList);
        recy_list.setAdapter(adapter);
        pushBroad = new PushBroad();
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                switch (view.getId()) {
                    case R.id.counseling_rela:

                        counselingListModel = counselingListModelList.get(position);
                        if (counselingListModel.getId().equals(Health_AppLocation.instance.users.getId())) {
                            Toast.makeText(Counselling_list_Activity.this, R.string.consult_to_me_error, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (Doctor_service_Activity.consultModelList.size() > 0) {
                            for (int i = 0; i < Doctor_service_Activity.consultModelList.size(); i++) {
                                if (Doctor_service_Activity.consultModelList.get(i).getDoctorId().equals(counselingListModel.getId())) {
                                    Toast.makeText(Counselling_list_Activity.this, R.string.consult_to_users_error, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }

                        FaceUrl = counselingListModel.getIconUrl();
                        titleStr = counselingListModel.getNickname();
                        getConsult(consultId);

                        break;

                    case R.id.item_rela:
                        Intent it = new Intent(Counselling_list_Activity.this,IM_Doctor_introduce_Activity.class);
                        it.putExtra("doctorId",counselingListModelList.get(position).getId());
                        startActivity(it);
                        break;

                }

//                getHistoryConsultList();

            }
        });
        consultId = getIntent().getStringExtra("consultId");
        getList(consultId);
    }

    private void getHistoryConsultList(){
        new PostUtils().Get(Constant.consult_history,true,hissory_handler,this);
    }
    private void getConsultList(){
        new PostUtils().Get(Constant.getConsultList,true,consult_list_handler,this);
    }

    /**
     * 获取单个咨询
     * @param consultId 咨询ID
     */
    private void getConsult(String consultId){
        String json = "consultId="+consultId;
        new PostUtils().Get(Constant.getconsult+json,true,consult_handler,this);
    }


    private void setIM_Icon(String url){
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setFaceUrl(url);

        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                it = new Intent(Counselling_list_Activity.this, ChatActivity.class);
                it.putExtra("identify", counselingListModel.getId());
                it.putExtra("type", TIMConversationType.C2C);
                it.putExtra("friendIcon",Constant.photo_IP+counselingListModel.getIconUrl());
                it.putExtra("myIcon",Health_AppLocation.instance.Icon.
                        substring(1,Health_AppLocation.instance.Icon.length()));
                it.putExtra("chatType",Constant.SELECTDOCTORSTATE);
                it.putExtra("ChatCount",ChatCount);
                it.putExtra("isDoctor",false);
                it.putExtra("consultId",consultId);
                it.putExtra("consult",true);//通过已邀请咨询列表进入
                Intent broadIntent = new Intent();
                broadIntent.setAction("finish");
                sendBroadcast(broadIntent);
                startActivity(it);
                finish();

            }

            @Override
            public void onSuccess() {
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                it = new Intent(Counselling_list_Activity.this, ChatActivity.class);
                it.putExtra("identify", counselingListModel.getId());
                it.putExtra("type", TIMConversationType.C2C);
                it.putExtra("friendIcon",Constant.photo_IP+counselingListModel.getIconUrl());
                it.putExtra("myIcon",Health_AppLocation.instance.Icon);
                it.putExtra("chatType",Constant.SELECTDOCTORSTATE);
                it.putExtra("ChatCount",ChatCount);
                it.putExtra("isDoctor",false);
                it.putExtra("consultId",consultId);
                it.putExtra("consult",true);//通过已邀请咨询列表进入
                Intent broadIntent = new Intent();
                broadIntent.setAction("finish");
                sendBroadcast(broadIntent);
                startActivity(it);
                finish();
            }
        });
    }



    private void getList(String consultId){
        String json = "consultId"+"="+consultId;
        new PostUtils().Get(Constant.getCounseling_list+json,true,Counseling_list_Handler,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getList(consultId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("updateCounselling_list");
        if(pushBroad!=null){
            registerReceiver(pushBroad,intentFilter);
        }else{
            pushBroad = new PushBroad();
            registerReceiver(pushBroad,intentFilter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(pushBroad!=null){
            unregisterReceiver(pushBroad);
        }
    }

    class PushBroad extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Thread.sleep(500);
                getList(consultId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
