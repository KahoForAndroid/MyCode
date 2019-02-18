package zj.health.health_v1.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
//import com.huawei.android.pushagent.PushManager;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.tlslibrary.service.TLSService;
//import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.AuxiliaryUtil.NotifyDialog;
import zj.health.health_v1.IM.model.UserInfo;
import zj.health.health_v1.IM.ui.SplashActivity;
import zj.health.health_v1.IM.ui.customview.DialogActivity;
import zj.health.health_v1.IM.utils.PushUtil;
import zj.health.health_v1.Model.Users;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Service.AlarmManageService;
import zj.health.health_v1.Utils.DbUtils;
import zj.health.health_v1.Utils.HttpUtil;
import zj.health.health_v1.Utils.SharedPreferencesUtils;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/3.
 */

public class Login_Activity extends BaseActivity implements View.OnClickListener{

    private Button Login_Button;
    private TextView get_Verification_Code_Text,new_user_text,switch_bind_text;
    private EditText username,code;
    private RelativeLayout share_weibo_rl,share_wechat_rl,share_qq_rl;
    private Intent intent = null;
    private TLSService tlsService;
    private final String TAG = Login_Activity.class.getSimpleName();
    private Timer timer;
    private int second = 60;//用于发送验证码倒数
    private Timertask timertask;
    private String id ,sig;
    private DbUtils dbUtils = new DbUtils();
    private Handler getVerification_Code_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                 if(msg.what == 200) {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if (jsonObject.opt("msg").equals("success")) {
                        timer = new Timer();
                        timertask = new Timertask();
                        timer.schedule(timertask, 1000, 1000);
                        Toast.makeText(Login_Activity.this, getString(R.string.send_verification_code_success), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login_Activity.this, getString(R.string.http_error_send_again), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(Login_Activity.this, msg.what+"code:"+jsonObject.optString("code"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Login_Activity.this, getString(R.string.http_error_send_again), Toast.LENGTH_SHORT).show();
//                     Toast.makeText(Login_Activity.this, msg.what, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                 e.printStackTrace();
            }
        }
    };
    private Handler isVerification_Success_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                if(jsonObject.optString("code").equals("0")){
                    JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                    if(jsonObject1.optBoolean("registered") == true){
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        Health_AppLocation.instance.Token = jsonObject1.optString("token");
                        JSONObject jsonObject2 = new JSONObject(jsonObject1.optString("user"));
                        Health_AppLocation.instance.Icon = jsonObject2.optString("iconUrl");
                        Health_AppLocation.instance.users = new Gson().fromJson(jsonObject1.optString("user"),Users.class);
                        Health_AppLocation.instance.users.setToken(jsonObject1.optString("token"));
//                        editor = SharedPreferencesUtils.
//                                getEditor(Login_Activity.this,"loginToken");
//                        id = jsonObject2.optString("id");
//                        editor.putString("id",id);
//                        editor.putString("token",Health_AppLocation.instance.Token);
//                        editor.putString("phone",jsonObject2.optString("phone"));
//                        editor.putString("nickname",jsonObject2.optString("nickname"));
//                        editor.putString("iconUrl",jsonObject2.optString("iconUrl"));
//                        editor.putString("doctorStatus",jsonObject2.optString("doctorStatus"));
//                        editor.commit();
                        getSig();
                    }else{
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        Toast.makeText(Login_Activity.this, getString(R.string.user_null), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                    Toast.makeText(Login_Activity.this, getString(R.string.verification_code_error), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private Handler SigHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        sig = jsonObject1.optString("sig");
                        SharedPreferences.Editor editor = SharedPreferencesUtils.
                                getEditor(Login_Activity.this,"loginToken");
                        editor.putString("phone",Health_AppLocation.instance.users.getPhone());
                        editor.commit();
                        Health_AppLocation.instance.users.setSig(sig);
                        Health_AppLocation.instance.CleanActivity();
                        UserInfo.getInstance().setId(Health_AppLocation.instance.users.getId());
                        UserInfo.getInstance().setUserSig(sig);
                        dbUtils.DelUsersData();
                        dbUtils.insertUsersData(Login_Activity.this,Health_AppLocation.instance.users);
                        intent = new Intent(Login_Activity.this,SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Login_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(Login_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        tlsService = TLSService.getInstance();
        initView();
        BindListener();
    }
    private void initView(){
        Login_Button = (Button)findViewById(R.id.Login_Button);
        get_Verification_Code_Text = (TextView)findViewById(R.id.get_Verification_Code_Text);
        new_user_text = (TextView)findViewById(R.id.new_user_text);
        switch_bind_text = (TextView)findViewById(R.id.switch_bind_text);
        share_weibo_rl = (RelativeLayout)findViewById(R.id.share_weibo_rl);
        share_wechat_rl = (RelativeLayout)findViewById(R.id.share_wechat_rl);
        share_qq_rl = (RelativeLayout)findViewById(R.id.share_qq_rl);
        username = (EditText)findViewById(R.id.username);
        code = (EditText)findViewById(R.id.password);
        SharedPreferences sp = SharedPreferencesUtils.getSharedPreferences(this,"loginToken");
        String phone = sp.getString("phone",null);
        if(phone!=null){
            username.setText(phone);
            username.setSelection(phone.length());
        }
    }

    private void BindListener(){
        Login_Button.setOnClickListener(this);
        get_Verification_Code_Text.setOnClickListener(this);
        new_user_text.setOnClickListener(this);
        switch_bind_text.setOnClickListener(this);
        share_weibo_rl.setOnClickListener(this);
        share_wechat_rl.setOnClickListener(this);
        share_qq_rl.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //登录Button
            case R.id.Login_Button:
                if(username.getText().toString().length() != 11){
                    Toast.makeText(this, R.string.input_phoneNumber_error, Toast.LENGTH_SHORT).show();
                      return;
                }
                if(code.getText().toString().length()!=6){
                    Toast.makeText(this, R.string.verification_code_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
                isVerificationSuccess(username.getText().toString(),code.getText().toString());
                break;
            //获取验证码
            case R.id.get_Verification_Code_Text:
                if(username.getText().toString().length() != 11){
                    Toast.makeText(this, R.string.input_phoneNumber_error, Toast.LENGTH_SHORT).show();
                }else{
                    getCode(username.getText().toString());
                }
                break;
            //新用户注册
            case R.id.new_user_text:
                intent = new Intent(this,New_register_Activity.class);
                startActivity(intent);
                break;
            //更改绑定
            case R.id.switch_bind_text:
                intent = new Intent(this,Replacement_binding_Frist_Activity.class);
                startActivity(intent);
                break;
            //微博分享
            case R.id.share_weibo_rl:
                CreateDialog dialog = new CreateDialog();
                dialog.MessageDailog(this,
                        View.inflate(this,R.layout.main_popwindow,null),
                        this);
                break;
            //微信分享
            case R.id.share_wechat_rl:
                LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
                break;
            //QQ分享
            case R.id.share_qq_rl:
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                break;
        }
    }

    private void getSig(){
        new PostUtils().Get(Constant.getSig,true,SigHandler,this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }



    private void getCode(String phone){
        new PostUtils().Get(Constant.getVerification_Code+"phone"+"="+phone,false,
                getVerification_Code_Handler,this);
    }


    private void isVerificationSuccess(String phone,String code){
        String getdata = "phone"+"="+phone+"&"+"code"+"="+code+"&"+"role"+"="+"1";
        new PostUtils().Get(Constant.verificationSuccess+getdata,false,
                isVerification_Success_Handler,this);
    }

    private class Timertask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(second > 0){
                        second --;
                        get_Verification_Code_Text.setEnabled(false);
                        get_Verification_Code_Text.setTextColor(getResources().getColor(R.color.color_69));
                        get_Verification_Code_Text.setText(getString(R.string.get_Verification_Code)+"("+second+")");
                    }else{
                        get_Verification_Code_Text.setEnabled(true);
                        get_Verification_Code_Text.setTextColor(getResources().getColor(R.color.main_color));
                        get_Verification_Code_Text.setText(getString(R.string.get_Verification_Code));
                        timertask.cancel();
                        second = 60;//恢复60秒
                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(timertask != null){
            timertask.cancel();
            timer = null;
        }
    }
}
