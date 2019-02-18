package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMAddFriendRequest;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendStatus;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import tencent.tls.tools.MD5;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.ui.ChatActivity;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GreenDaoManager;
import zj.health.health_v1.Utils.HttpUtil;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;


/**
 * Created by Administrator on 2018/10/24.
 */

public class PayActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title,cost_text;
    private Button pay_button;
    private String cost,orderId,consultId,doctorId;
    private String FaceUrl= null;
    private int ChatCount = 0;
    private int status;
    private String titleStr = null;
    private boolean Ispayment = false;//是否已支付
    private Handler WechatRepay_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        PayReq req = new PayReq();
                        req.appId = jsonObject1.optString("appID");
                        req.partnerId = jsonObject1.optString("partnerID");
                        req.prepayId = jsonObject1.optString("prepayId");
                        req.packageValue = jsonObject1.optString("packag");
                        req.nonceStr = jsonObject1.optString("noncestr");
                        req.timeStamp = jsonObject1.optString("timestamp");
                        req.sign = jsonObject1.optString("sign");
                        Health_AppLocation.instance.iwxapi.sendReq(req);
                    }else{
                        Toast.makeText(PayActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(PayActivity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(PayActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };

    private Handler choose_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        cost = jsonObject1.optString("cost");
                        orderId = jsonObject1.optString("orderId");
                        Ispayment = true;
                        Wechat_prepay(orderId);
//                        Intent it = new Intent(PayActivity.this, WXPayEntryActivity.class);
//                        it.putExtra("cost",cost);
//                        it.putExtra("orderId",orderId);
//                        startActivityForResult(it,0x1122);
                    }else{
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        Toast.makeText(PayActivity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(PayActivity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(PayActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };

    private Handler updateConversation_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        GreenDaoManager.getInstance().getSession().getConsultTimeModelDao().deleteAll();
                        getChatCount(consultId);

                    }else{
//                        LoadingDialo g.getLoadingDialog().StopLoadingDialog();
                        Toast.makeText(PayActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                }
            }else if(msg.what == Constant.UserErrorCode){
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(PayActivity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(PayActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };

    private Handler  getChatCountHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        //获取当前会话的聊天次数
                        ChatCount = jsonObject1.optInt("count");
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        if(StringUtil.isEmpty(FaceUrl)){
//                            setIM_Icon(Constant.photo_IP+counselingListModel.getIconUrl());
                            FaceUrl = "";
                        }
                        Intent it = new Intent(PayActivity.this, ChatActivity.class);
                        it.putExtra("identify", doctorId);
                        it.putExtra("doctorId", doctorId);
                        it.putExtra("type", TIMConversationType.C2C);
                        it.putExtra("myIcon",Health_AppLocation.instance.Icon);
                        it.putExtra("friendIcon",FaceUrl);
                        it.putExtra("chatType",Constant.SELECTDOCTORSTATE);
                        it.putExtra("ChatCount",ChatCount);
                        it.putExtra("isDoctor",false);
                        it.putExtra("consultId",consultId);
                        it.putExtra("titleStr",titleStr);
                        it.putExtra("consult",true);//通过已邀请咨询列表进入
                        Intent broadIntent = new Intent();
                        broadIntent.setAction("finish");
                        sendBroadcast(broadIntent);
                        startActivity(it);
                        finish();

                    }else{
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        Toast.makeText(PayActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                }
            }else if(msg.what == Constant.UserErrorCode){
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(PayActivity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(PayActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };


    private Handler CheckPayHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        if(jsonObject.optString("code").equals("0")){
                            GreenDaoManager.getInstance().getSession().getConsultTimeModelDao().deleteAll();
                            AddIMFriend(doctorId);
                        }else{
                            Toast.makeText(PayActivity.this, getString(R.string.pay_false), Toast.LENGTH_SHORT).show();
                        }

                    }else if(jsonObject.optString("msg").equals("failed")){
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        Toast.makeText(PayActivity.this, getString(R.string.pay_false), Toast.LENGTH_SHORT).show();
                    }else{
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        Toast.makeText(PayActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                }
            }else if(msg.what == Constant.UserErrorCode){
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(PayActivity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(PayActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        title = (TextView)findViewById(R.id.title);
        back = (ImageView)findViewById(R.id.back);
        pay_button = (Button)findViewById(R.id.pay_button);
        cost_text = (TextView)findViewById(R.id.cost_text);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        pay_button.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.pay));
        consultId = getIntent().getStringExtra("consultId");
        doctorId = getIntent().getStringExtra("doctorId");
        FaceUrl = getIntent().getStringExtra("FaceUrl");
        titleStr = getIntent().getStringExtra("titleStr");
        status = getIntent().getIntExtra("status",0);
        cost = String.valueOf(getIntent().getFloatExtra("cost",0));
        cost_text.setText(StringUtil.trimNull(cost));

    }
    private void user_Choose(String consultId,String doctorId){
        Map<String,String> map = new HashMap<>();
        map.put("consultId",consultId);
        map.put("doctorId",doctorId);
        new PostUtils().getNewPost(Constant.user_choose,map,choose_Handler,this);
    }
    /**
     * 更新健康咨询聊天会话ID
     * @param consultId
     * @param conversationId
     */
    private void updateConversationId(String consultId,String conversationId){
//        LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
        Map<String,String> map = new HashMap<>();
        map.put("consultId",consultId);
        map.put("conversationId",conversationId);
        new PostUtils().getNewPost(Constant.updateConversationId,map,updateConversation_Handler,this);
    }
    /**
     * 获取当前聊天次数
     */
    private void getChatCount(String consultId){
        String data = "consultId"+"="+consultId;
        new PostUtils().Get(Constant.getChatCount+data,true,getChatCountHandler,this);
    }

    private void CheckPayStatus(){
        String json = "orderId="+orderId;
        new PostUtils().Get(Constant.Wechat_pay_check+json,true,CheckPayHandler,this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.pay_button:
//                if(status == 3){
//                    LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
//                    user_Choose(consultId,doctorId);
//                }
                LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
                user_Choose(consultId,doctorId);
                break;
                default:
                    break;
        }
    }

    private void Wechat_prepay(String orderid){
        String json = "orderId="+orderid;
        new PostUtils().Get(Constant.Wechat_prepay+json,true,WechatRepay_Handler,this);
    }

    /**
     * 添加为腾讯云IM好友
     * @param Identifier
     */
    private void AddIMFriend(final String Identifier) {
        //创建请求列表
        List<TIMAddFriendRequest> reqList = new ArrayList<TIMAddFriendRequest>();

        //添加好友请求
        TIMAddFriendRequest req = new TIMAddFriendRequest(Identifier);
//        req.setAddrSource("DemoApp");
//        req.setAddWording("add me");
//        req.setRemark("Cat");
//        req.setIdentifier(Identifier);
        reqList.add(req);

        //申请添加好友
        TIMFriendshipManagerExt.getInstance().addFriend(reqList, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
            }

            @Override
            public void onSuccess(List<TIMFriendResult> result) {
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
//                if(result.get(0).getStatus() == TIMFriendStatus.TIM_ADD_FRIEND_STATUS_ALREADY_FRIEND ||
//                        result.get(0).getStatus() == TIMFriendStatus.TIM_FRIEND_STATUS_SUCC){
//                    getIMData_toFriend(result.get(0).getIdentifer());
//                }else{
//                    Toast.makeText(PayActivity.this, R.string.go_toIm_error, Toast.LENGTH_SHORT).show();
//                }
//                getIMData_toFriend(result.get(0).getIdentifer());
                updateConversationId(consultId,doctorId+Health_AppLocation.instance.users.getId());
            }
        });
    }

    private void getIMData_toFriend(String Identifier){
        LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
        //获取自己的资料
        //待获取用户资料的好友列表
        List<String> users = new ArrayList<String>();
        users.add(Identifier);

//获取好友资料
        TIMFriendshipManagerExt.getInstance().getFriendsProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
            }

            @Override
            public void onSuccess(List<TIMUserProfile> result){
//                FaceUrl = result.get(0).getFaceUrl();
//                titleStr = result.get(0).getNickName();
                updateConversationId(consultId,doctorId+Health_AppLocation.instance.users.getId());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Ispayment){
            LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
            CheckPayStatus();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @NonNull
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Toast.makeText(this, R.string.pay_success, Toast.LENGTH_SHORT).show();
            AddIMFriend(doctorId);
        }else{
            Toast.makeText(this, R.string.pay_false, Toast.LENGTH_SHORT).show();
        }
    }
}
