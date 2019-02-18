package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/3.
 */

public class New_register_Activity extends BaseActivity implements View.OnClickListener{

    private TextView title;
    private ImageView back;
    private RelativeLayout get_Verification_Code_rl;
    private EditText ed_phone,ed_Verification_Code,ed_referral;
    private TextView get_Verification_Code_text;
    private Button next_btn;
    private Timer timer;
    private int second = 60;//用于发送验证码倒数
    private Timertask timertask;
    private Handler getVerification_Code_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                if(jsonObject.opt("msg").equals("success")){
                    timer = new Timer();
                    timertask = new Timertask();
                    timer.schedule(timertask,1000,1000);
                    Toast.makeText(New_register_Activity.this, getString(R.string.send_verification_code_success), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(New_register_Activity.this,   getString(R.string.http_error_send_again), Toast.LENGTH_SHORT).show();
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
                     Health_AppLocation.instance.Token = jsonObject1.optString("token");
                    if(!jsonObject1.optBoolean("registered")){
                        Intent it = new Intent(New_register_Activity.this,Register_Security_Activity.class);
                        if(ed_referral.getText().toString().length()>0){
                            it.putExtra("referral",ed_referral.getText().toString());
                        }
                        it.putExtra("phone",ed_phone.getText().toString());
                        startActivity(it);
                    }else{
                        Toast.makeText(New_register_Activity.this, R.string.is_register, Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(New_register_Activity.this, getString(R.string.verification_code_error), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_register_activity);
        initView();
        BindListener();
    }

    private void initView(){
        get_Verification_Code_rl = (RelativeLayout)findViewById(R.id.get_Verification_Code_rl);
        ed_Verification_Code = (EditText)findViewById(R.id.ed_Verification_Code);
        ed_phone = (EditText)findViewById(R.id.ed_phone);
        title = (TextView)findViewById(R.id.title);
        back = (ImageView)findViewById(R.id.back);
        next_btn = (Button)findViewById(R.id.next_btn);
        ed_referral = (EditText)findViewById(R.id.ed_referral);
        get_Verification_Code_text = (TextView)findViewById(R.id.get_Verification_Code_text);
        title.setText(R.string.register_title);
    }

   private void BindListener(){
       back.setOnClickListener(this);
       next_btn.setOnClickListener(this);
       get_Verification_Code_rl.setOnClickListener(this);
  }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.get_Verification_Code_rl:
                if(ed_phone.getText().toString().length() == 11){
                    getCode(ed_phone.getText().toString());
                }else{
                    Toast.makeText(this, getString(R.string.input_phoneNumber_error), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.next_btn:
                if(ed_phone.getText().toString().length() == 11 && ed_Verification_Code.getText().toString().length() == 6){
                    isVerificationSuccess(ed_phone.getText().toString(),ed_Verification_Code.getText().toString());
                }else{
                    Toast.makeText(this, R.string.next_error, Toast.LENGTH_SHORT).show();

                }
                break;
                default:
                    break;
        }
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

    private class Timertask extends TimerTask{

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(second > 0){
                        second --;
                        get_Verification_Code_rl.setEnabled(false);
                        get_Verification_Code_rl.setBackgroundColor(getResources().getColor(R.color.image_overlay_false));
                        get_Verification_Code_text.setText(getString(R.string.get_Verification_Code)+"("+second+")");
                    }else{
                        get_Verification_Code_rl.setEnabled(true);
                        get_Verification_Code_rl.setBackground(getResources().getDrawable(R.drawable.layou_onclick));
                        get_Verification_Code_text.setText(getString(R.string.get_Verification_Code));
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
