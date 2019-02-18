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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/15.
 */

public class Replacement_binding_Last_Activity extends BaseActivity implements View.OnClickListener{

    private TextView title,get_Verification_Code_text;
    private ImageView back;
    private RelativeLayout get_Verification_Code_rl;
    private EditText ed_phone,ed_Verification_Code;
    private Button next_btn;
    private int selectType;
    private String sqId,answer,oldphone,friendphone,code;
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
                    Toast.makeText(Replacement_binding_Last_Activity.this, getString(R.string.send_verification_code_success), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Replacement_binding_Last_Activity.this, getString(R.string.http_error_send_again), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler rebind_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                if(jsonObject.opt("msg").equals("success")){
                    Intent it = new Intent(Replacement_binding_Last_Activity.this,
                            Replacement_binding_Finish_Activity.class);
                    startActivity(it);
                }else{
                    Toast.makeText(Replacement_binding_Last_Activity.this, jsonObject.opt("msg").toString(), Toast.LENGTH_SHORT).show();
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
        setDate();

    }

    private void initView(){
        get_Verification_Code_rl = (RelativeLayout)findViewById(R.id.get_Verification_Code_rl);
        ed_Verification_Code = (EditText)findViewById(R.id.ed_Verification_Code);
        get_Verification_Code_text = (TextView)findViewById(R.id.get_Verification_Code_text);
        ed_phone = (EditText)findViewById(R.id.ed_phone);
        title = (TextView)findViewById(R.id.title);
        back = (ImageView)findViewById(R.id.back);
        next_btn = (Button)findViewById(R.id.next_btn);
    }

    private void BindListener(){
        back.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        get_Verification_Code_rl.setOnClickListener(this);
    }
    private void setDate(){
        title.setText(R.string.Replacement_binding_phone);
        ed_phone.setHint(R.string.input_new_phone);
        next_btn.setText(R.string.binding_phone);
        selectType = getIntent().getIntExtra("selectType",0);
        if(selectType == 1){
            sqId = getIntent().getStringExtra("sqId");
            answer = getIntent().getStringExtra("answer");
            oldphone = getIntent().getStringExtra("oldphone");
        }else if(selectType == 2){
            oldphone = getIntent().getStringExtra("oldphone");
            friendphone = getIntent().getStringExtra("friendphone");
            code = getIntent().getStringExtra("code");
        }
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
               if(ed_phone.getText().toString().length() == 11 &&
                       ed_Verification_Code.getText().toString().length() == 6){
                   JSONObject jsonObject1 = new JSONObject();
                   try{
                       if(selectType == 1){
                           jsonObject1.put("sqId",sqId);
                           jsonObject1.put("sqAnswer",answer);
                           jsonObject1.put("oldPhone",oldphone);
                           jsonObject1.put("newPhone",ed_phone.getText().toString());
                           jsonObject1.put("newCode",ed_Verification_Code.getText().toString());
                           rebind_post(Constant.security_question,jsonObject1);
                       }else if(selectType == 2){
                           jsonObject1.put("oldPhone",oldphone);
                           jsonObject1.put("friendPhone",friendphone);
                           jsonObject1.put("friendCode",code);
                           jsonObject1.put("newPhone",ed_phone.getText().toString());
                           jsonObject1.put("newCode",ed_Verification_Code.getText().toString());
                           rebind_post(Constant.friend_to_send_newPhone,jsonObject1);
                       }
                   }catch (Exception e){
                       e.printStackTrace();
                   }

               }else{
                   Toast.makeText(this, getString(R.string.next_error), Toast.LENGTH_SHORT).show();
               }
                break;
        }
    }


    private void getCode(String phone){
        new PostUtils().Get(Constant.new_phone+"phone"+"="+phone,false,
                getVerification_Code_Handler,this);
    }


    private void rebind_post(String postName,JSONObject jsonObject){
        new PostUtils().JsonPost(postName,jsonObject,rebind_Handler,this);
    }

    private class Timertask extends TimerTask {

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
}

